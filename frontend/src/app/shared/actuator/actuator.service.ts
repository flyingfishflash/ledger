// angular
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

// third party
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

import { AppConfig } from "app/app-config";
import { ActuatorInfo } from "./actuator-info";

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
