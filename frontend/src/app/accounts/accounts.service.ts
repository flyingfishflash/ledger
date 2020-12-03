import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError, map } from "rxjs/operators";

import { AppConfig } from "../app-config";
import { list_to_tree_sorted } from "src/app/_helpers/tree-utilities";
import { IAccount } from "./account";
import { ActivatedRoute, Router } from "@angular/router";
import { BasicAuthService } from "../_services/basic-auth.service";

@Injectable({
  providedIn: "root",
})
export class AccountsService {
  // private accountUrl = '/api/accounts/accounts.json';

  constructor(
    private appConfig: AppConfig,
    private basicAuthService: BasicAuthService,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  getAccounts(): Observable<any> {
    return this.http.get<any>(`${this.appConfig.apiServer.url}/accounts`).pipe(
      map((res) => {
        return res.response.body;
      }),
      catchError(this.handleError)
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
    return this.http.get<any>(`${this.appConfig.apiServer.url}/accounts`).pipe(
      map((res) => {
        return list_to_tree_sorted(res.response.body);
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
      .get<any>(`${this.appConfig.apiServer.url}/account-categories`)
      .pipe(
        map((res) => {
          return res.response.body;
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
    let errorMessage = "";
    if (error.error instanceof ErrorEvent) {
      errorMessage = "A client internal error occurred: " + error.error.message;
    } else {
      console.log(
        "Error type: " + Object.getPrototypeOf(error).constructor.name
      );
    }
    return throwError(errorMessage);
  }
}
