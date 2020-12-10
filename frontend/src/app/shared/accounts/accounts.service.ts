// angular
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { ActivatedRoute } from "@angular/router";
import { Router } from "@angular/router";

// third party
import { Observable } from "rxjs";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { map } from "rxjs/operators";

// core and shared
import { AppConfig } from "app/app-config";
import { BasicAuthService } from "app/core/authentication/basic-auth.service";
import { TreeUtilitiesService } from "@shared/tree-utilities/tree-utilties.service";
import { IAccount } from "./account";

@Injectable({
  providedIn: "root",
})
export class AccountsService {
  // private accountUrl = '/api/accounts/accounts.json';

  constructor(
    private appConfig: AppConfig,
    private basicAuthService: BasicAuthService,
    private treeUtilitiesService: TreeUtilitiesService,
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
        return this.treeUtilitiesService.list_to_tree_sorted(res.response.body);
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
      errorMessage = "A non-client error occured";
    }
    return throwError(errorMessage);
  }
}
