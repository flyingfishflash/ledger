// angular
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

// third party
import { Observable, throwError } from "rxjs";
import { catchError, map } from "rxjs/operators";

// core and shared
import { AppConfig } from "app/app-config";
import { ActuatorInfo } from "./actuator-info";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("actuator.service");

@Injectable({
  providedIn: "root",
})
export class ActuatorService {
  constructor(private appConfig: AppConfig, private http: HttpClient) {}

  getInfo(): Observable<ActuatorInfo> {
    return this.http
      .get<ActuatorInfo>(`${this.appConfig.apiServer.url}/actuator/info`)
      .pipe(
        map((res) => {
          return res;
        }),
        catchError(this.handleError)
      );
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
