// angular
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

// third party
import { Observable, ReplaySubject, Subject, throwError } from "rxjs";
import { catchError } from "rxjs/operators";

// core and shared
import { environment } from "@env";
import { Logger } from "@core/logging/logger.service";
import { StorageService } from "@core/storage/storage.service";

const log = new Logger("profile.service");

const httpOptions = {
  // eslint-disable-next-line @typescript-eslint/naming-convention
  headers: new HttpHeaders({ "Content-Type": "application/json" }),
  withCredentials: true,
};

@Injectable({
  providedIn: "root",
})
export class ProfileService {
  private loggedInUserId;
  private subject: Subject<any> = new ReplaySubject<any>(1);
  private profileUpdateStatus: Subject<any> = new ReplaySubject<any>(1);

  constructor(
    private http: HttpClient,
    private storageService: StorageService
  ) {
    this.loggedInUserId = this.storageService.getLoggedInUserId();
  }

  get $getSubject(): Observable<any> {
    return this.subject.asObservable();
  }

  get $getProfileUpdateStatus(): Observable<any> {
    return this.profileUpdateStatus.asObservable();
  }

  loadData() {
    this.http
      .get<any>(
        environment.api.server.url +
          "/users/" +
          this.loggedInUserId +
          "/profile"
      )
      .subscribe(
        (res) => {
          this.subject.next(res.content);
        },
        (err) => {
          this.handleError(err);
        }
      );
  }

  loadDataById(id) {
    this.http
      .get<any>(environment.api.server.url + "/users/" + id + "/profile")
      .subscribe(
        (res) => {
          this.subject.next(res.content);
        },
        (err) => {
          this.handleError(err);
        }
      );
  }

  userDetailsUpdate(payload, id) {
    this.http
      .patch<any>(
        environment.api.server.url + "/users/" + id,
        {
          email: payload.email,
          firstName: payload.firstName,
          lastName: payload.lastName,
          password: payload.password,
        },
        httpOptions
      )
      .subscribe(
        (res) => {
          this.subject.next(res.content);
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
        (err) => {
          this.handleError(err);
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
        environment.api.server.url + "/users/" + this.loggedInUserId,
        { payload },
        httpOptions
      )
      .pipe(catchError(this.handleError));
  }

  handleError(error: any) {
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
