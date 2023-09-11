import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Router } from '@angular/router'
import { Observable, of, throwError } from 'rxjs'
import { catchError, retry } from 'rxjs/operators'
import { BasicAuthService } from '../../core/authentication/basic-auth.service'
import { Logger } from '../../core/logging/logger.service'
import { ErrorDialogService } from '../error-dialog/error-dialog.service'

const log = new Logger('http-error.interceptor')

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  private errorState = {
    errorType: '',
    errorStatus: 0,
    errorMessageOriginal: '',
    errorMessageHeading: '',
    errorMessageLine1: '',
    errorMessageLine2: '',
  }

  constructor(
    private authenticationService: BasicAuthService,
    private errorDialogService: ErrorDialogService,
    private router: Router,
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    let isHandled = false

    return next.handle(request).pipe(
      retry(1),
      catchError((error) => {
        let errorMessage: string = ''

        log.debug(error)

        if (error.error instanceof ErrorEvent) {
          errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`
          this.errorState.errorType = 'client'
          this.errorState.errorMessageOriginal = error.error.message
          this.errorState.errorMessageHeading =
            'A client internal error occurred'
          this.errorState.errorMessageLine1 = error.error.message
        } else if (error instanceof HttpErrorResponse) {
          errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`
          this.errorState.errorType = 'server'
          this.errorState.errorStatus = error.status
          this.errorState.errorMessageOriginal = error.message
          this.errorState.errorMessageHeading = errorMessage
          this.errorState.errorMessageLine1 = error.message
          isHandled = this.handleServerSideError(error)
        }

        log.error(errorMessage ? errorMessage : error)

        if (!isHandled) {
          if (errorMessage!) {
            return throwError(() => error)
          } else {
            return throwError(() => 'Unexpected problem occurred')
          }
        } else {
          return of(error)
        }
      }),
    )
  }

  private handleServerSideError(error: HttpErrorResponse): boolean {
    let isHandled = false
    log.debug(error.status)
    const errorUrl: string = error.url == null ? '' : error.url
    switch (error.status) {
      case 0:
        // for the time being don't mark http status code 0 errors as handled
        // so they're returned to the calling component
        break
      case 400:
      case 500:
        log.info(error.error.content.title)
        this.errorDialogService.openDialog(
          error.error.content.detail ?? JSON.stringify(error),
          error.status,
          error.error.content.title,
        )
        isHandled = true
        log.debug('init1')
        break
      case 401:
        if (!errorUrl.includes('signin') && !errorUrl.includes('/health')) {
          this.authenticationService.redirectToLogin()
          this.errorDialogService.openDialog(
            error.error.content.detail ?? JSON.stringify(error),
            error.status,
            error.error.content.title,
          )
          isHandled = true
        }
        break
      case 403:
        if (!errorUrl.includes('signin')) {
          this.authenticationService.redirectToLogin()
          this.errorDialogService.openDialog(
            error.error.content.detail ?? JSON.stringify(error),
            error.status,
            error.error.content.title,
          )
          isHandled = true
        }
        break
      case 404:
        if (!errorUrl.includes('config.json')) {
          this.errorDialogService.openDialog(
            error.error.content.detail ?? JSON.stringify(error),
            error.status,
            error.error.content.title,
          )
          isHandled = true
        }
        break
      case 502:
        if (!errorUrl.includes('config.json')) {
          this.errorState.errorMessageHeading = `Server Error: ${error.status} Bad Gateway`
          this.errorState.errorMessageLine1 =
            "Runtime configuration couldn't be found. Perhaps the server isn't up or isn't accepting connections."
          this.router.navigate(['/error'], { state: this.errorState })
          isHandled = true
        }
        break
      default:
        window.sessionStorage.clear()
        log.debug(this.errorState)
        this.router.navigate(['/error'], { state: this.errorState })
        isHandled = true
    }
    return isHandled
  }
}
