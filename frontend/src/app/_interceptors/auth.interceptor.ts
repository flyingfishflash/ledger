import {
  HTTP_INTERCEPTORS,
  HttpResponse,
  HttpEvent,
  HttpErrorResponse,
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import {
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
} from "@angular/common/http";
import { catchError, map } from "rxjs/operators";
import { throwError, Observable } from "rxjs";

import { BasicAuthService } from "../_services/basic-auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private basicAuthService: BasicAuthService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let authReq = req;

    /*
      Intercepted request by authenticated user
    */
    if (
      this.basicAuthService.isUserLoggedIn() //&& req.url.indexOf("signin") === -1
    ) {
      //console.log("authorized http request intercept");
      authReq = req.clone({
        headers: req.headers
          .append("Access-Control-Allow-Credentials", "true")
          .append("X-Auth-Token", this.basicAuthService.getSessionId())
          .append("X-Requested-With", "XMLHttpRequest"),
        withCredentials: true,
      });
      /*       return next.handle(authReq).pipe(
        map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            console.log("authorized http response intercept");
            // console.log("X-Auth-Token: ", event.headers.get("X-Auth-Token"));
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
          console.log(errorMessage);
          return throwError(errorMessage);
        })
      ); */
    } else {
      /*
        Intercepted request by non-authenticated user
      */
      /*       console.log("anonymous http request intercept");
      return next.handle(req).pipe(
        map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            console.log("anonymous http response intercept");
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
          console.log(errorMessage);
          return throwError(errorMessage);
        })
      ); */
    }
    return next.handle(authReq);
  }
}
