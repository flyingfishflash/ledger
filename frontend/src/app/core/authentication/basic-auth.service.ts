// angular
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Router } from '@angular/router'

// third party
import { BehaviorSubject, Observable, throwError } from 'rxjs'
import { tap } from 'rxjs/operators'

// core and shared
import { environment } from '../../../environments/environment'
import { Logger } from '../../core/logging/logger.service'
import { StorageService } from '../../core/storage/storage.service'
import { BasicAuthUser } from './basic-auth-user'

const log = new Logger('basic-auth.service')

@Injectable({ providedIn: 'root' })
export class BasicAuthService {
  public user: Observable<any>

  private userSubject: BehaviorSubject<BasicAuthUser>

  constructor(
    private router: Router,
    private http: HttpClient,
    private storageService: StorageService,
  ) {
    this.userSubject = new BehaviorSubject<BasicAuthUser>(
      storageService.getAuthenticatedUser(),
    )
    this.user = this.userSubject.asObservable()
  }

  public get userValue(): BasicAuthUser {
    return this.userSubject.value
  }

  signIn(credentials) {
    // if POST set 'Content-Type': 'application/json'
    const headers = new HttpHeaders(
      credentials
        ? {
            authorization: this.createBasicAuthToken(
              credentials.username,
              credentials.password,
            ),
          }
        : {},
    )

    return this.http
      .get<any>(environment.api.server.url + '/auth/signin', {
        observe: 'response',
        headers,
        withCredentials: true,
      })
      .pipe(
        tap((res: HttpResponse<any>) => {
          const u = new BasicAuthUser(res)
          // TODO: remove me
          this.storageService.saveActiveBookId(48748)
          this.storageService.saveAuthenticatedUser(u)
          this.userSubject.next(u)
          return u
        }),
      )
  }

  createBasicAuthToken(username, password) {
    return 'Basic ' + window.btoa(username + ':' + password)
  }

  signOut(parameter: string) {
    this.http
      .post<any>(environment.api.server.url + '/auth/signout', {
        parameter,
      })
      .subscribe({
        next: (response) => {
          if (response.message === 'Successful sign-out') {
            window.sessionStorage.clear()
            this.userSubject.next(null)
            this.router.navigate(['/login'])
          }
        },
        error: (error) => {
          this.handleError(error)
        },
      })
  }

  redirectToLogin() {
    window.sessionStorage.clear()
    this.userSubject.next(null)
    //this.userSubject.unsubscribe();
    this.router.navigate(['/login'])
  }

  handleError(error: any) {
    let errorMessage = ''
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`
    } else {
      errorMessage = `A server-side error occured on SIGN-OUT:\nError Status: ${error.status}\nError Message: ${error.message}`
    }
    log.error(errorMessage)
    return throwError(() => error)
  }
}
