// angular
import { HttpRequest } from "@angular/common/http";
import { HttpHandler } from "@angular/common/http";
import { HttpEvent } from "@angular/common/http";
import { HttpInterceptor } from "@angular/common/http";
import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";

// third party
import { Observable } from "rxjs";
import { throwError } from "rxjs";
import { catchError } from "rxjs/operators";

// core and shared
import { BasicAuthService } from "@core/authentication/basic-auth.service";
import { ErrorDialogService } from "@shared/errors/error-dialog.service";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("http-error.interceptor");

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(
    private authenticationService: BasicAuthService,
    private errorDialogService: ErrorDialogService
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = "";
        if (error.error instanceof ErrorEvent) {
          errorMessage = `Error: ${error.error.message}`;
        } else {
          errorMessage = `Error Status: ${error.status}\nError Message: ${error.message}`;

          if (request.url.indexOf("signin") === -1) {
            if ([401, 403].indexOf(error.status) !== -1) {
              this.authenticationService.redirectToLogin();
            }
            this.errorDialogService.openDialog(
              error.error.response.body.message ?? JSON.stringify(error),
              error.status
            );
          } else {
            // errors signing in should be displayed using the login component, not via dialog box
          }
        }
        log.error(errorMessage);
        return throwError(error);
      })
    );
  }
}
