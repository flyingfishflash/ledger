import { Injectable } from "@angular/core";
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from "@angular/common/http";
import { Observable, throwError, BehaviorSubject } from "rxjs";
import { AppConfig } from "../app-config";
import { User } from "../_models/user";
import { Router } from "@angular/router";
import { tap } from "rxjs/internal/operators/tap";

@Injectable({ providedIn: "root" })
export class BasicAuthService {
  STORAGE_KEY_AUTHENTICATED_USER = "authenticated-user";

  private userSubject: BehaviorSubject<User>;
  public user: Observable<any>;

  constructor(
    private router: Router,
    private http: HttpClient,
    private appConfig: AppConfig
  ) {
    this.userSubject = new BehaviorSubject<User>(
      JSON.parse(
        window.sessionStorage.getItem(this.STORAGE_KEY_AUTHENTICATED_USER)
      )
    );
    this.user = this.userSubject.asObservable();
  }

  public get userValue(): User {
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
        headers: headers,
        withCredentials: true,
      })
      .pipe(
        tap((resp) => {
          const u = new User();
          u.id = resp.body.response.body.id;
          u.username = resp.body.response.body.username;
          u.roles = resp.body.response.body.roles;
          u.sessionId = resp.headers.get("x-auth-token");
          window.sessionStorage.setItem(
            this.STORAGE_KEY_AUTHENTICATED_USER,
            JSON.stringify(u)
          );
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
    this.userSubject.unsubscribe;
    this.router.navigate([""]);
  }

  isUserLoggedIn() {
    if (this.userSubject.getValue() == null) {
      return false;
    }
    return true;
  }

  getLoggedInUserId() {
    try {
      const id = this.userSubject.getValue().id;
      if (id === null) {
        //console.log("id is null");
        return null;
      }
      //console.log("id is not null");
      //console.log(id);
      return id;
    } catch (err) {
      console.log("caught getloggedinuserid error, redirecting to login");
      console.log(err);
      this.redirectToLogin();
    }
  }

  getLoggedInUserName() {
    const username = this.userSubject.getValue().username;
    if (username === null) {
      return "";
    }
    return username;
  }

  getSessionId() {
    const sessionId = this.userSubject.getValue().sessionId;
    if (sessionId === null) {
      return "";
    }
    return sessionId;
  }

  handleError(err: HttpErrorResponse) {
    let errorMessage = "";
    if (err.error instanceof ErrorEvent) {
      errorMessage = "An error occurred: " + err.error.message;
    } else {
      errorMessage = "HttpErrorResponse: " + err.status + " / " + err.message;
    }
    console.log("Error handled.");
    // console.log(err);
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
