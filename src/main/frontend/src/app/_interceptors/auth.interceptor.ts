import { HTTP_INTERCEPTORS, HttpHeaders, HttpResponse, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';

import { BasicAuthService } from '../_services/basic-auth.service';
import { TokenStorageService } from '../_services/token-storage.service';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private token: TokenStorageService,
    private basicAuthService: BasicAuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    // const nonAuthReq = req;

    if (this.basicAuthService.isUserLoggedIn() && req.url.indexOf('signin') === -1) {
      console.log('authorized http request intercept');
      authReq = req.clone({
        headers: req.headers
          .append('Access-Control-Allow-Credentials', 'true')
          .append('Authorization', this.basicAuthService.getBasicAuthenticationToken())
          .append('X-Requested-With', 'XMLHttpRequest')
        , withCredentials: true
      });
      return next.handle(authReq)
        .pipe(
          map((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
              console.log('authorized http response intercept');
            }
            return event;
          }),
          catchError(error => {
            console.log('Error response status: ', error.status);
            if (error.status === 401) {
              console.log('401 ERROR');
            }
            return throwError(error);
          }));
    } else {
      console.log('anonymous http request intercept');
      return next.handle(req)
        .pipe(
          map((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
              console.log('anonymous http response intercept');
            }
            return event;
          }),
          catchError(error => {
            console.log('Error response status: ', error.status);
            if (error.status === 401) {
              console.log('401 ERROR');
            }
            return throwError(error);
          }));
    }
  }

  intercept1(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const xAuthToken = this.token.getXAuthToken();

    if (this.basicAuthService.isUserLoggedIn() && req.url.indexOf('signin') === -1) {
      console.log('authreq');
      console.log(xAuthToken);
      const authReq = req.clone({
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'X-Requested-With': 'XMLHttpRequest',
          'Access-Control-Allow-Credentials': 'true',
          'X-Auth-Token': xAuthToken,
          // 'Authorization': sessionStorage.getItem(SESSION_ATTRIBUTE_BASIC_AUTH_TOKEN)
        }), withCredentials: true
      });
      return next.handle(authReq)
        .pipe(
          map((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
            }
            return event;
          }),
          catchError(error => {
            console.log('Error response status: ', error.status);
            if (error.status === 401) {
              console.log('401 ERROR');
            }
            return throwError(error);
          }));

    } else {
      return next.handle(req);
    }
  }

  interceptBasicAuth(req: HttpRequest<any>, next: HttpHandler) {
    // Basic Authentication
    let authReq = req;
    authReq = req.clone({
      headers: req.headers
        .append('X-Requested-With', 'XMLHttpRequest')
    });
    return next.handle(authReq)
      .pipe(
        map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
          }
          return event;
        }),
        catchError(error => {
          console.log('Error response status: ', error.status);
          if (error.status === 401) {
            console.log('401 ERROR');
          }
          return throwError(error);
        }));
  }


  private interceptTokenAuth(req: HttpRequest<any>, next: HttpHandler) {
    let authReq = req;
    // let newReq = req;
    // const token = this.token.getToken();
    const xAuthToken = this.token.getXAuthToken();
    // if (token != null) {
    authReq = req.clone({
      headers: req.headers
      // .set(STORAGE_KEY_TOKEN_HEADER, 'Bearer ' + token)
      // .append('X-Requested-With', 'XMLHttpRequest')
      // .append('Access-Control-Allow-Credentials', 'true' )
      // .append('X-Auth-Token', xAuthToken)
      // , withCredentials: true
    });
    // }
    return next.handle(authReq)
      .pipe(
        map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            if (xAuthToken == null) {
              this.token.saveXAuthToken(event.headers.get('X-Auth-Token'));
            }
            console.log('Header: ', event.headers.get('X-Auth-Token'));
          }
          return event;
        }),
        catchError(error => {
          console.log('Error response status: ', error.status);
          if (error.status === 401) {
            console.log('401 ERROR');
            // this.userService.setLoggedUser(null);
          }
          return throwError(error);
        }));
  }
}

export const authInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];
