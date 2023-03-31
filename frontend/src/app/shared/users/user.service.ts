// angular
import { Injectable } from "@angular/core";
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
} from "@angular/common/http";
import { EVENT_MANAGER_PLUGINS } from "@angular/platform-browser";

// third party
import { Observable, throwError } from "rxjs";
import { catchError, map } from "rxjs/operators";

// core and shared
import { AppConfigRuntime } from "../../app-config-runtime";
import { Logger } from "@core/logging/logger.service";
import { StorageService } from "@core/storage/storage.service";

const log = new Logger("user.service");

const httpOptions = {
  // eslint-disable-next-line @typescript-eslint/naming-convention
  headers: new HttpHeaders({ "Content-Type": "application/json" }),
  withCredentials: true,
};

@Injectable({
  providedIn: "root",
})
export class UserService {
  constructor(
    private appConfig: AppConfigRuntime,
    private http: HttpClient,
    private storageService: StorageService
  ) {}

  findAllUsers(): Observable<any> {
    return this.http
      .get<any>(`${this.appConfig.assets.api.server.url}/users`)
      .pipe(
        map((res) => {
          return res.content;
        }),
        catchError(this.handleError)
      );
  }

  userDetailsUpdate(payload): Observable<any> {
    log.debug(payload);
    return this.http
      .patch<any>(
        this.appConfig.assets.api.server.url +
          "/users/" +
          this.storageService.getAuthenticatedUser().id,
        {
          email: payload.email,
          firstName: payload.firstName,
          lastName: payload.lastName,
          password: payload.password,
        },
        httpOptions
      )
      .pipe(catchError(this.handleError));
  }

  userSignUp(userFormValue): Observable<any> {
    const roles: string[] = [];
    roles.push(userFormValue.role);

    return this.http
      .post<any>(
        this.appConfig.assets.api.server.url + "/users/",
        {
          email: userFormValue.email,
          firstName: userFormValue.firstName,
          lastName: userFormValue.lastName,
          password: userFormValue.password,
          roles,
          username: userFormValue.username,
        },
        httpOptions
      )
      .pipe(catchError(this.handleError));
  }

  userDeleteById(id) {
    const payload: any = {};
    payload.id = id;
    const httpParams = new HttpParams().set("id", id);
    const options = { params: httpParams, withCredentials: true };

    this.http
      .delete<any>(
        this.appConfig.assets.api.server.url + "/users/delete",
        options
      )
      .subscribe(
        (successResponse) => {
          log.debug(successResponse);
          // this.subject.next(successResponse.content);
          // const fields = Object.getOwnPropertyNames(payload);
          // const ps = new Date().toLocaleTimeString() + ': ' + 'Updated profile (' + fields.toString().replace(/,/g, ', ').split(/(?=[A-Z])/).map(s => s.toLowerCase()).join(' ') + ')';
          // this.profileUpdateStatus.next(ps);
        },
        (ledgerErrorResponse) => {
          this.handleError(ledgerErrorResponse);
        }
      );
  }

  handleError(httpErrorResponse: HttpErrorResponse) {
    return throwError(httpErrorResponse);
  }
}
