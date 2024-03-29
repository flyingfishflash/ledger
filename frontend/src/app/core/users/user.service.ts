import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
} from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable, throwError } from 'rxjs'
import { catchError, map } from 'rxjs/operators'
import { environment } from '../../../environments/environment'
import { Logger } from '../logging/logger.service'
import { StorageService } from '../storage/storage.service'

const log = new Logger('user.service')

const httpOptions = {
  // eslint-disable-next-line @typescript-eslint/naming-convention
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true,
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private http: HttpClient,
    private storageService: StorageService,
  ) {}

  findAllUsers(): Observable<any> {
    return this.http.get<any>(`${environment.api.server.url}/users`).pipe(
      map((res) => {
        return res.content
      }),
      catchError(this.handleError),
    )
  }

  userDetailsUpdate(payload: any): Observable<any> {
    log.debug(payload)
    return this.http
      .patch<any>(
        environment.api.server.url +
          '/users/' +
          this.storageService.getAuthenticatedUser().id,
        {
          email: payload.email,
          firstName: payload.firstName,
          lastName: payload.lastName,
          password: payload.password,
        },
        httpOptions,
      )
      .pipe(catchError(this.handleError))
  }

  userSignUp(userFormValue: any): Observable<any> {
    const roles: string[] = []
    roles.push(userFormValue.role)

    return this.http
      .post<any>(
        environment.api.server.url + '/users',
        {
          email: userFormValue.email,
          firstName: userFormValue.firstName,
          lastName: userFormValue.lastName,
          password: userFormValue.password,
          roles,
          username: userFormValue.username,
        },
        httpOptions,
      )
      .pipe(catchError(this.handleError))
  }

  userDeleteById(id: number) {
    const payload: any = {}
    payload.id = id
    const httpParams = new HttpParams().set('id', id)
    const options = { params: httpParams, withCredentials: true }

    this.http
      .delete<any>(environment.api.server.url + '/users/delete', options)
      .subscribe({
        next: (successResponse) => {
          log.debug(successResponse)
          // this.subject.next(successResponse.content);
          // const fields = Object.getOwnPropertyNames(payload);
          // const ps = new Date().toLocaleTimeString() + ': ' + 'Updated profile (' + fields.toString().replace(/,/g, ', ').split(/(?=[A-Z])/).map(s => s.toLowerCase()).join(' ') + ')';
          // this.profileUpdateStatus.next(ps);
        },
        error: (ledgerErrorResponse) => {
          this.handleError(ledgerErrorResponse)
        },
      })
  }

  handleError(httpErrorResponse: HttpErrorResponse) {
    return throwError(() => httpErrorResponse)
  }
}
