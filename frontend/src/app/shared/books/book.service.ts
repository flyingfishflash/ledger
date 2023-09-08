// angular
import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { Injectable } from '@angular/core'

// third party
import { Observable, throwError } from 'rxjs'
import { catchError } from 'rxjs/operators'

// core and shared
import { environment } from '../../../environments/environment'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('books.service')

@Injectable({
  providedIn: 'root',
})
export class BookService {
  constructor(private http: HttpClient) {}

  postBook(bookName: string): Observable<any> {
    return this.http
      .post<any>(environment.api.server.url + '/books', {
        name: bookName,
      })
      .pipe(catchError(this.handleError))
  }

  handleError(httpErrorResponse: HttpErrorResponse) {
    return throwError(() => httpErrorResponse)
  }
}
