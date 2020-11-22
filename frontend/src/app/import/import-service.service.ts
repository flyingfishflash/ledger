import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Observable, Subject, timer, throwError } from "rxjs";
import {
  map,
  switchMap,
  retry,
  tap,
  share,
  takeUntil,
  catchError,
} from "rxjs/operators";

import { AppConfig } from "../app-config";

@Injectable({
  providedIn: "root",
})
export class ImportService {
  private allCurrencies$: Observable<any[]>;

  private stopPolling = new Subject();

  constructor(private appConfig: AppConfig, private http: HttpClient) {
    console.log("constructing import service");

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

    console.log(this.allCurrencies$);
  }

  getImportStatus(): Observable<any> {
    console.log("getImportStatus()");
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
    console.log("in getAllCurrencies");
    return this.allCurrencies$.pipe(
      tap(() => console.log("data sent to subscriber!!!"))
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
    console.log("Error handled.");
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
