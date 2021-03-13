// angular
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

// third party
import { BehaviorSubject, Observable, throwError } from "rxjs";
import { tap } from "rxjs/internal/operators/tap";

// core and shared
import { AppConfig } from "app/app-config";
import { BasicAuthUser } from "./basic-auth-user";
import { Logger } from "@core/logging/logger.service";
import { StorageService } from "@core/storage/storage.service";

const log = new Logger("basic-auth.service");

@Injectable({ providedIn: "root" })
export class BasicAuthService {
  public user: Observable<any>;

  private userSubject: BehaviorSubject<BasicAuthUser>;

  constructor(
    private router: Router,
    private http: HttpClient,
    private appConfig: AppConfig,
    private storageService: StorageService
  ) {
    this.userSubject = new BehaviorSubject<BasicAuthUser>(
      storageService.getAuthenticatedUser()
    );
    this.user = this.userSubject.asObservable();
  }

  public get userValue(): BasicAuthUser {
    return this.userSubject.value;
  }

  signIn(credentials) {
    // if POST set 'Content-Type': 'application/json'
    const headers = new HttpHeaders(
      credentials
        ? {
            authorization: this.createBasicAuthToken(
              credentials.username,
              credentials.password
            ),
          }
        : {}
    );

    return this.http
      .get<any>(this.appConfig.apiServer.url + "/auth/signin", {
        observe: "response",
        headers,
        withCredentials: true,
      })
      .pipe(
        tap((resp) => {
          const u = new BasicAuthUser(resp);
          this.storageService.saveAuthenticatedUser(u);
          this.userSubject.next(u);
          return u;
        })
      );
  }

  createBasicAuthToken(username, password) {
    return "Basic " + window.btoa(username + ":" + password);
  }

  signOut(parameter: string) {
    this.http
      .post<any>(this.appConfig.apiServer.url + "/auth/signout", { parameter })
      .subscribe(
        (result) => {
          if (result.response.body.message === "Logged Out") {
            window.sessionStorage.clear();
            this.userSubject.next(null);
            this.router.navigate(["/login"]);
          }
        },
        (err) => {
          this.handleError(err);
        }
      );
  }

  redirectToLogin() {
    window.sessionStorage.clear();
    this.userSubject.next(null);
    //this.userSubject.unsubscribe();
    this.router.navigate(["/login"]);
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
