// angular
import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";

// third party
import { Observable, throwError } from "rxjs";
import { catchError, map } from "rxjs/operators";

// core and shared
import { environment } from "@env";
import { BasicAuthService } from "app/core/authentication/basic-auth.service";
import { TreeUtilitiesService } from "@shared/tree-utilities/tree-utilties.service";
import { StorageService } from "@core/storage/storage.service";
import { IAccount } from "./account";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("account.service");

@Injectable({
  providedIn: "root",
})
export class AccountsService {
  // private accountUrl = '/api/accounts/accounts.json';

  constructor(
    private basicAuthService: BasicAuthService,
    private treeUtilitiesService: TreeUtilitiesService,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private storageService: StorageService
  ) {}

  getAccounts(): Observable<any> {
    const httpParams = new HttpParams().set(
      "bookId",
      this.storageService.getActiveBookId()
    );

    return this.http
      .get<any>(`${environment.api.server.url}/accounts`, {
        params: httpParams,
      })
      .pipe(
        map((res) => {
          return res.content;
        }) //,
        //catchError(this.handleError)
      );
  }

  /*
         getAccounts(): Observable<any> {
             return this.http.get<IAccount[]>(`${this.appConfig.apiServer.url}/accounts`).pipe(
                 map(res => res), catchError(this.handleError)
             );
         }
    */

  getAccountsTree(): Observable<any> {
    const httpParams = new HttpParams().set(
      "bookId",
      this.storageService.getActiveBookId()
    );

    return this.http
      .get<any>(`${environment.api.server.url}/accounts`, {
        params: httpParams,
      })
      .pipe(
        map((res) => {
          return this.treeUtilitiesService.listToTreeSorted(res.content);
        }),
        catchError(this.handleError)
      );
  }

  /*
         getAccountsTree(): Observable<any> {
             return this.http.get<IAccount[]>(`${this.appConfig.apiServer.url}/accounts`).pipe(
                 map(res => list_to_tree_sorted(res))
            );
        }
     */

  getAccountCategories(): Observable<string[]> {
    return this.http
      .get<any>(`${environment.api.server.url}/account-categories`)
      .pipe(
        map((res) => {
          return res.content;
        }),
        catchError(this.handleError)
      );
  }

  /*
         getAccountCategories(): Observable<string[]> {
             return this.http.get<string[]>(`${this.appConfig.apiServer.url}/account-categories`).pipe(
                map(res => res), catchError(this.handleError)
            );
        }
    */

  handleError(error: any) {
    log.info("zzzzzz");
    log.debug(`error: ${error}`);
    let errorMessage = "";
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`;
    } else {
      errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`;
    }
    log.error(errorMessage);
    return throwError(error);
  }
}
