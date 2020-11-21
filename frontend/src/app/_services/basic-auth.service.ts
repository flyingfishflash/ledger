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
import { map } from "rxjs/internal/operators/map";

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
        headers: headers,
        withCredentials: true,
      })
      .pipe(
        map((user) => {
          const u = new User();
          u.id = user.response.body.id;
          u.username = user.response.body.username;
          u.token = this.createBasicAuthToken(
            user.response.body.username,
            credentials.password
          );
          u.roles = user.response.body.roles;
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

  isUserLoggedIn() {
    if (this.userSubject.getValue() == null) {
      return false;
    }
    return true;
  }

  getLoggedInUserId() {
    const id = this.userSubject.getValue().id;
    if (id === null) {
      console.log("id is null");
      return null;
    }
    console.log("id is not null");
    console.log(id);
    return id;
  }

  getLoggedInUserName() {
    const username = this.userSubject.getValue().username;
    if (username === null) {
      return "";
    }
    return username;
  }

  getBasicAuthenticationToken() {
    const token = this.userSubject.getValue().token;
    if (token === null) {
      return "";
    }
    return token;
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
