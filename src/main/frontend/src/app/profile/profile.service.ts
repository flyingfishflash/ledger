import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, Subject, ReplaySubject } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { BasicAuthService } from '../_services/basic-auth.service'

const API = environment.api.url;

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
};

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  loggedInUserId;

  constructor(
    private http: HttpClient,
    private authenticationService: BasicAuthService
  ) {
    this.loggedInUserId = authenticationService.getLoggedInUserId();
  }

  private subject: Subject<any> = new ReplaySubject<any>(1);

  private profileUpdateStatus: Subject<any> = new ReplaySubject<any>(1);

  get $getSubject(): Observable<any> {
    return this.subject.asObservable();
  }

  get $getProfileUpdateStatus(): Observable<any> {
    return this.profileUpdateStatus.asObservable();
  }

  loadData() {
    this.http.get<any>(API + '/users/' + this.loggedInUserId + '/profile')
      .subscribe(data => {
        this.subject.next(data.response.body);
      });
  }

  loadDataById(id) {
    this.http.get<any>(API + '/users/' + id + '/profile')
      .subscribe(data => {
        this.subject.next(data.response.body);
      });
  }

  userDetailsUpdate(payload, id) {
    this.http.patch<any>(API + '/users/' + id, {
      email: payload.email,
      firstName: payload.firstName,
      lastName: payload.lastName,
      password: payload.password
    }, httpOptions)
      .subscribe(
        successResponse => {
          this.subject.next(successResponse.response.body);
          const fields = Object.getOwnPropertyNames(payload);
          const ps = new Date().toLocaleTimeString() + ': ' + 'Updated profile (' + fields.toString().replace(/,/g, ', ').split(/(?=[A-Z])/).map(s => s.toLowerCase()).join(' ') + ')';
          this.profileUpdateStatus.next(ps);
        },
        errorResponse => {
          this.handleError(errorResponse);
        }
      );
  }

  resetStatus() {
    this.profileUpdateStatus.next(null);
  }

  userDetailsUpdateOriginal(payload): Observable<any> {
    console.log(payload);
    return this.http.patch(API + '/users/' + this.loggedInUserId, { payload }, httpOptions).pipe(catchError(this.handleError));
  }

  handleError(err: HttpErrorResponse) {
    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      errorMessage = 'An error occurred: ' + err.error.message;
    } else {
      errorMessage = 'HttpErrorResponse: ' + err.status + ' / ' + err.message;
    }
    console.log('Error handled.');
    // console.log(err);
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
