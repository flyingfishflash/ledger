import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { BasicAuthService } from "../_services/basic-auth.service";
import { ErrorDialogService } from "../shared/errors/error-dialog.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
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
        console.log(errorMessage);
        return throwError(error);
      })
    );
  }
}
