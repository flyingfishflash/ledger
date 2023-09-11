import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable, Subject, throwError, timer } from 'rxjs'
import {
  catchError,
  map,
  retry,
  share,
  switchMap,
  takeUntil,
  tap,
} from 'rxjs/operators'
import { environment } from '../../../environments/environment'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('import.service')

@Injectable({
  providedIn: 'root',
})
export class ImportService {
  private allCurrencies$: Observable<any[]>

  private stopPolling = new Subject()

  constructor(private http: HttpClient) {
    this.allCurrencies$ = timer(1, 1000).pipe(
      switchMap(() =>
        http.get<any[]>(
          `${environment.api.server.url}/import/gnucashFileImportStatus`,
        ),
      ),
      retry(),
      tap(console.log),
      share(),
      takeUntil(this.stopPolling),
    )

    log.debug(this.allCurrencies$)
  }

  getImportStatus(): Observable<any> {
    log.debug('getImportStatus()')
    return this.http
      .get<any[]>(
        `${environment.api.server.url}/import/gnucashFileImportStatus`,
      )
      .pipe(
        map((res) => res),
        catchError(this.handleError),
      )
  }

  getAllCurrencies(): Observable<any[]> {
    log.debug('in getAllCurrencies')
    return this.allCurrencies$.pipe(
      tap(() => log.debug('data sent to subscriber!!!')),
    )
  }

  uploadFile(formData: any): Observable<any> {
    return this.http
      .post<any[]>(`${environment.api.server.url}/import/gnucash`, formData)
      .pipe(
        map((res) => {
          return res
        }),
      )
  }

  handleError(err: HttpErrorResponse) {
    let errorMessage = ''
    if (err.error instanceof ErrorEvent) {
      errorMessage = 'An error occurred: ' + err.error.message
    } else {
      errorMessage = 'HttpErrorResponse: ' + err.status + ' / ' + err.message
    }
    log.debug('Error handled.')
    log.error(errorMessage)
    return throwError(() => errorMessage)
  }
}
