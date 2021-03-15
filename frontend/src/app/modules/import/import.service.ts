// angular
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";

// third party
import { Observable, Subject, timer, throwError } from "rxjs";
import {
  catchError,
  map,
  switchMap,
  retry,
  tap,
  share,
  takeUntil,
} from "rxjs/operators";

// core and shared
import { AppConfig } from "app/app-config";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("import.service");

@Injectable({
  providedIn: "root",
})
export class ImportService {
  private allCurrencies$: Observable<any[]>;

  private stopPolling = new Subject();

  constructor(private appConfig: AppConfig, private http: HttpClient) {
    this.allCurrencies$ = timer(1, 1000).pipe(
      switchMap(() =>
        http.get<any[]>(
          `${this.appConfig.apiServer.url}/import/gnucashFileImportStatus`
        )
      ),
      retry(),
      tap(console.log),
      share(),
      takeUntil(this.stopPolling)
    );

    log.debug(this.allCurrencies$);
  }

  getImportStatus(): Observable<any> {
    log.debug("getImportStatus()");
    return this.http
      .get<any[]>(
        `${this.appConfig.apiServer.url}/import/gnucashFileImportStatus`
      )
      .pipe(
        map((res) => res),
        catchError(this.handleError)
      );
  }

  getAllCurrencies(): Observable<any[]> {
    log.debug("in getAllCurrencies");
    return this.allCurrencies$.pipe(
      tap(() => log.debug("data sent to subscriber!!!"))
    );
  }

  uploadFile(formData): Observable<any> {
    return this.http
      .post<any[]>(`${this.appConfig.apiServer.url}/import/gnucash`, formData)
      .pipe(
        map((res) => {
          return res;
        })
      );
  }

  handleError(err: HttpErrorResponse) {
    let errorMessage = "";
    if (err.error instanceof ErrorEvent) {
      errorMessage = "An error occurred: " + err.error.message;
    } else {
      errorMessage = "HttpErrorResponse: " + err.status + " / " + err.message;
    }
    log.debug("Error handled.");
    log.error(errorMessage);
    return throwError(errorMessage);
  }
}
