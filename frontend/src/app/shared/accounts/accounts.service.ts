import { HttpClient, HttpParams } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { Observable, throwError } from 'rxjs'
import { catchError, map } from 'rxjs/operators'

import { environment } from '../../../environments/environment'
import { BasicAuthService } from '../../core/authentication/basic-auth.service'
import { Logger } from '../../core/logging/logger.service'
import { StorageService } from '../../core/storage/storage.service'

const log = new Logger('account.service')

@Injectable({
  providedIn: 'root',
})
export class AccountsService {
  // private accountUrl = '/api/accounts/accounts.json';

  constructor(
    private basicAuthService: BasicAuthService,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private storageService: StorageService,
  ) {}

  getAccounts(): Observable<any> {
    const httpParams = new HttpParams().set(
      'bookId',
      this.storageService.getActiveBookId(),
    )

    return this.http
      .get<any>(`${environment.api.server.url}/accounts`, {
        params: httpParams,
      })
      .pipe(
        map((res) => {
          return res.content
        }), //,
        //catchError(this.handleError)
      )
  }

  getAccountCategories(): Observable<string[]> {
    return this.http
      .get<any>(`${environment.api.server.url}/account-categories`)
      .pipe(
        map((res) => {
          return res.content
        }),
        catchError(this.handleError),
      )
  }

  handleError(error: any) {
    log.info('zzzzzz')
    log.debug(`error: ${error}`)
    let errorMessage = ''
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`
    } else {
      errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`
    }
    log.error(errorMessage)
    return throwError(() => error)
  }
}
