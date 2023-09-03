// angular
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Injectable } from '@angular/core'

// third party
import { Observable, ReplaySubject, Subject, throwError } from 'rxjs'
import { catchError, map } from 'rxjs/operators'

// core and shared
import { environment } from '../../../environments/environment'
import { Logger } from '../../core/logging/logger.service'
import { StorageService } from '../../core/storage/storage.service'

const log = new Logger('profile.service')

const httpOptions = {
  // eslint-disable-next-line @typescript-eslint/naming-convention
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  withCredentials: true,
}

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  constructor(private http: HttpClient) {}

  getProfileById(id: number): Observable<any> {
    return this.http
      .get<any>(environment.api.server.url + '/users/' + id + '/profile')
      .pipe(
        map((res) => {
          return res.content
        }), //,
        catchError(this.handleError),
      )
  }

  userDetailsUpdate(payload, id) {
    this.http
      .patch<any>(
        environment.api.server.url + '/users/' + id,
        {
          email: payload.email,
          firstName: payload.firstName,
          lastName: payload.lastName,
          password: payload.password,
        },
        httpOptions,
      )
      .subscribe({
        next: (response) => {
          // this.subject.next(res.content)
          const fields = Object.getOwnPropertyNames(payload)
          log.debug(response)
          // const ps =
          //   new Date().toLocaleTimeString() +
          //   ': ' +
          //   'Updated profile (' +
          //   fields
          //     .toString()
          //     .replace(/,/g, ', ')
          //     .split(/(?=[A-Z])/)
          //     .map((s) => s.toLowerCase())
          //     .join(' ') +
          //   ')'
          // this.profileUpdateStatus.next(ps)
        },
        error: (err) => {
          this.handleError(err)
        },
      })
  }

  handleError(error: any) {
    let errorMessage = ''
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`
    } else {
      errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`
    }
    log.error(errorMessage)
    return throwError(error)
  }
}
