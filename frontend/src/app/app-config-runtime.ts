// angular
import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";

// third party
import { EMPTY, Observable } from "rxjs";
import { catchError, map, mergeMap } from "rxjs/operators";

// core and shared
import { Logger } from "@core/logging/logger.service";
import { AppConfigRuntimeApiServer } from "./app-config-runtime-api-server";
import { AppConfigRuntimeInfoBuild } from "./app-config-runtime-info-build";

const log = new Logger("app-config");

export interface IAppConfigRuntime extends AppConfigRuntime {
  assets: {
    api: {
      server: AppConfigRuntimeApiServer;
    };
  };

  api: {
    actuator: {
      info: {
        build: AppConfigRuntimeInfoBuild;
      };
    };
  };

  load: () => Observable<AppConfigRuntime>;
}

@Injectable()
export class AppConfigRuntime implements IAppConfigRuntime {
  public assets = { api: { server: null } };
  public api = { actuator: { info: { build: null } } };

  constructor(private readonly http: HttpClient) {}

  public load(): Observable<AppConfigRuntime> {
    return this.http.get("assets/config.json").pipe(
      mergeMap((server: any) => {
        this.assets.api.server = server.server;
        return this.http
          .get(this.assets.api.server.url + "/actuator/info")
          .pipe(
            map((build: any) => {
              this.api.actuator.info.build = build.build;
              return build;
            }),
            catchError((err) => {
              this.handleError(err);
              return EMPTY;
            })
          );
      }),
      catchError((err) => {
        this.handleError(err);
        return EMPTY;
      })
    );
  }

  handleError(error: any) {
    let errorMessage = "";
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`;
    } else if (error instanceof TypeError) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.message}`;
    } else if (error instanceof HttpErrorResponse) {
      errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`;
    } else {
      errorMessage = "An error of an unknown type occured:";
      log.error(error);
    }
    log.error(errorMessage);
  }
}
