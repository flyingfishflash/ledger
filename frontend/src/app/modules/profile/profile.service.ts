// angular
import { HttpClient } from "@angular/common/http";
import { HttpErrorResponse } from "@angular/common/http";
import { HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

// third party
import { Observable } from "rxjs";
import { ReplaySubject } from "rxjs";
import { Subject } from "rxjs";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";

// core and shared
import { AppConfig } from "app/app-config";
import { Logger } from "@core/logging/logger.service";
import { StorageService } from "@core/storage/storage.service";

const log = new Logger("profile.service");

const httpOptions = {
  headers: new HttpHeaders({ "Content-Type": "application/json" }),
  withCredentials: true,
};

@Injectable({
  providedIn: "root",
})
export class ProfileService {
  loggedInUserId;

  constructor(
    private appConfig: AppConfig,
    private http: HttpClient,
    private storageService: StorageService
  ) {
    this.loggedInUserId = storageService.getLoggedInUserId();
  }

  private subject: Subject<any> = new ReplaySubject<any>(1);

  private profileUpdateStatus: Subject<any> = new ReplaySubject<any>(1);

  get $getSubject(): Observable<any> {
    return this.subject.asObservable();
  }

  get $getProfileUpdateStatus(): Observable<any> {
    return this.profileUpdateStatus.asObservable();
  }

  loadData() {
    this.http
      .get<any>(
        this.appConfig.apiServer.url +
          "/users/" +
          this.loggedInUserId +
          "/profile"
      )
      .subscribe((data) => {
        this.subject.next(data.response.body);
      });
  }

  loadDataById(id) {
    this.http
      .get<any>(this.appConfig.apiServer.url + "/users/" + id + "/profile")
      .subscribe((data) => {
        this.subject.next(data.response.body);
      });
    // TODO: Handle error
  }

  userDetailsUpdate(payload, id) {
    this.http
      .patch<any>(
        this.appConfig.apiServer.url + "/users/" + id,
        {
          email: payload.email,
          firstName: payload.firstName,
          lastName: payload.lastName,
          password: payload.password,
        },
        httpOptions
      )
      .subscribe(
        (successResponse) => {
          this.subject.next(successResponse.response.body);
          const fields = Object.getOwnPropertyNames(payload);
          const ps =
            new Date().toLocaleTimeString() +
            ": " +
            "Updated profile (" +
            fields
              .toString()
              .replace(/,/g, ", ")
              .split(/(?=[A-Z])/)
              .map((s) => s.toLowerCase())
              .join(" ") +
            ")";
          this.profileUpdateStatus.next(ps);
        },
        (errorResponse) => {
          this.handleError(errorResponse);
        }
      );
  }

  resetStatus() {
    this.profileUpdateStatus.next(null);
  }

  userDetailsUpdateOriginal(payload): Observable<any> {
    log.debug(payload);
    return this.http
      .patch(
        this.appConfig.apiServer.url + "/users/" + this.loggedInUserId,
        { payload },
        httpOptions
      )
      .pipe(catchError(this.handleError));
  }

  handleError(err: HttpErrorResponse) {
    let errorMessage = "";
    if (err.error instanceof ErrorEvent) {
      errorMessage = "An error occurred: " + err.error.message;
    } else {
      errorMessage = "HttpErrorResponse: " + err.status + " / " + err.message;
    }
    log.debug("Error handled.");
    // log.debug(err);
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
