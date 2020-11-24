import { Injectable } from "@angular/core";
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from "@angular/common/http";
import { AppConfig } from "../app-config";
import { Observable } from "rxjs";
import { catchError, map } from "rxjs/operators";
import { ActuatorInfo } from "../_models/actuator-info";

@Injectable({
  providedIn: "root",
})
export class ActuatorService {
  private build: Observable<any>;

  constructor(private appConfig: AppConfig, private http: HttpClient) {}

  getInfo(): Observable<ActuatorInfo> {
    return this.http
      .get<ActuatorInfo>(`${this.appConfig.apiServer.url}/actuator/info`)
      .pipe(
        map((response) => {
          return response;
        })
        //catchError(this.handleError)
      );
  }
}
