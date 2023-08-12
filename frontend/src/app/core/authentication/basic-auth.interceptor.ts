// angular
import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';

// third party
import { Observable } from 'rxjs';

// core and shared
import { StorageService } from '../../core/storage/storage.service';
import { Logger } from '../../core/logging/logger.service';

const log = new Logger('basic-auth.interceptor');

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private storageService: StorageService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    let authReq = req;
    /*
      Intercepted request by authenticated user
    */
    if (this.storageService.getAuthenticatedUser()) {
      authReq = req.clone({
        headers: req.headers
          .append('Access-Control-Allow-Credentials', 'true')
          .append(
            'X-Auth-Token',
            this.storageService.getAuthenticatedUser().sessionId,
          )
          .append('X-Requested-With', 'XMLHttpRequest'),
        withCredentials: true,
      });
      /*       return next.handle(authReq).pipe(
        map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            log.debug("authorized http response intercept");
            // log.debug("X-Auth-Token: ", event.headers.get("X-Auth-Token"));
          }
          return event;
        }),
        catchError((error: HttpErrorResponse) => {
          let errorMessage = "";
          if (error.error instanceof ErrorEvent) {
            errorMessage = `Error: ${error.error.message}`;
          } else {
            errorMessage = `Error Status: ${error.status}\nError Message: ${error.message}`;
            if (error.status === 401) {
              this.basicAuthService.redirectToLogin();
            }
          }
          log.error(errorMessage);
          return throwError(errorMessage);
        })
      ); */
    } else {
      /*
        Intercepted request by non-authenticated user
      */
      /*       log.debug("anonymous http request intercept");
      return next.handle(req).pipe(
        map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            log.debug("anonymous http response intercept");
          }
          return event;
        }),
        catchError((error: HttpErrorResponse) => {
          let errorMessage = "";
          if (error.error instanceof ErrorEvent) {
            errorMessage = `Error: ${error.error.message}`;
          } else {
            errorMessage = `Error Status: ${error.status}\nError Message: ${error.message}`;
          }
          log.error(errorMessage);
          return throwError(errorMessage);
        })
      ); */
    }
    return next.handle(authReq);
  }
}
