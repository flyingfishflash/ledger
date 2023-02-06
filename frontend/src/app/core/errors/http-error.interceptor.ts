// angular
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

// third party
import { Observable, throwError, of } from "rxjs";
import { catchError, retry } from "rxjs/operators";

// core and shared
import { BasicAuthService } from "@core/authentication/basic-auth.service";
import { ErrorDialogService } from "@shared/errors/error-dialog.service";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("http-error.interceptor");

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  private errorState = {
    errorType: null,
    errorStatus: null,
    errorMessageOriginal: null,
    errorMessageHeading: null,
    errorMessageLine1: null,
    errorMessageLine2: null,
  };

  constructor(
    private authenticationService: BasicAuthService,
    private errorDialogService: ErrorDialogService,
    private router: Router
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let handled = false;

    return next.handle(request).pipe(
      retry(1),
      catchError((error) => {
        let errorMessage = null;

        log.debug(error);

        if (error.error instanceof ErrorEvent) {
          errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`;
          this.errorState.errorType = "client";
          this.errorState.errorMessageOriginal = error.error.message;
          this.errorState.errorMessageHeading =
            "A client internal error occurred";
          this.errorState.errorMessageLine1 = error.error.message;
        } else if (error instanceof HttpErrorResponse) {
          errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`;
          this.errorState.errorType = "server";
          this.errorState.errorStatus = error.status;
          this.errorState.errorMessageOriginal = error.message;
          this.errorState.errorMessageHeading = errorMessage;
          this.errorState.errorMessageLine1 = error.message;
          handled = this.handleServerSideError(error);
        }

        log.error(errorMessage ? errorMessage : error);

        if (!handled) {
          if (errorMessage) {
            return throwError(error);
          } else {
            return throwError("Unexpected problem occurred");
          }
        } else {
          return of(error);
        }
      })
    );
  }

  private handleServerSideError(error: HttpErrorResponse): boolean {
    let handled = false;
    log.debug(error.status);
    switch (error.status) {
      case 0:
        // for the time being don't mark http status code 0 errors as handled
        // so they're returned to the calling component
        break;
      case 400:
      case 500:
        log.info(error.error.response.body.title);
        this.errorDialogService.openDialog(
          error.error.response.body.detail ?? JSON.stringify(error),
          error.status,
          error.error.response.body.title
        );
        handled = true;
        log.debug("init1");
        break;
      case 401:
        if (error.url.indexOf("signin") < 0) {
          this.authenticationService.redirectToLogin();
          this.errorDialogService.openDialog(
            error.error.response.body.detail ?? JSON.stringify(error),
            error.status,
            error.error.response.body.title
          );
          handled = true;
        }
        break;
      case 403:
        if (error.url.indexOf("signin") < 0) {
          this.authenticationService.redirectToLogin();
          this.errorDialogService.openDialog(
            error.error.response.body.detail ?? JSON.stringify(error),
            error.status,
            error.error.response.body.title
          );
          handled = true;
        }
        break;
      case 404:
        if (error.url.indexOf("config.json") < 0) {
          this.errorDialogService.openDialog(
            error.error.response.body.detail ?? JSON.stringify(error),
            error.status,
            error.error.response.body.title
          );
          handled = true;
        }
        break;
      case 502:
        if (error.url.indexOf("config.json") < 0) {
          this.errorState.errorMessageHeading = `Server Error: ${error.status} Bad Gateway`;
          this.errorState.errorMessageLine1 =
            "Runtime configuration couldn't be found. Perhaps the server isn't up or isn't accepting connections.";
          this.router.navigate(["/error"], { state: this.errorState });
          handled = true;
        }
        break;
      default:
        window.sessionStorage.clear();
        log.debug(this.errorState);
        this.router.navigate(["/error"], { state: this.errorState });
        handled = true;
    }
    return handled;
  }
}
