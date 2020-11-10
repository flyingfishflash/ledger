import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, throwError, Subject, ReplaySubject } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { BasicAuthService } from './basic-auth.service';
import { EVENT_MANAGER_PLUGINS } from '@angular/platform-browser';

const API = environment.api.url;

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }), withCredentials: true
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  loggedInUserId;

  constructor(
    private http: HttpClient,
    private authenticationService: BasicAuthService) {
    this.loggedInUserId = authenticationService.getLoggedInUserId();
  }

  findAllUsers(): Observable<any> {
    return this.http.get<any>(`${API}/users`).pipe(
      map(res => {
        return res.response.body;
      }), catchError(this.handleError)
    );
  }

  userDetailsUpdate(payload): Observable<any> {
    console.log(payload);
    return this.http.patch<any>(API + '/users/' + this.loggedInUserId, {
      email: payload.email,
      firstName: payload.firstName,
      lastName: payload.lastName,
      password: payload.password,
     }, httpOptions).pipe(catchError(this.handleError));
  }

  userSignUp(userFormValue): Observable<any> {
    let roles: string[] = [];
    roles.push(userFormValue.role);

    return this.http.post<any>(API + '/users/', {
      email: userFormValue.email,
      firstName: userFormValue.firstName,
      lastName: userFormValue.lastName,
      password: userFormValue.password,
      roles: roles,
      username: userFormValue.username
    }, httpOptions).pipe(catchError(this.handleError));
  }

  userDeleteById(id) {
    const payload: any = {};
    payload.id = id;
    console.log('iserDe');
    let httpParams = new HttpParams().set('id', id);
    let options = { params: httpParams, withCredentials: true };

    this.http.delete<any>(API + '/users/delete', options, )
      .subscribe(
        successResponse => {
          console.log(successResponse);
          // this.subject.next(successResponse.response.body);
          // const fields = Object.getOwnPropertyNames(payload);
          // const ps = new Date().toLocaleTimeString() + ': ' + 'Updated profile (' + fields.toString().replace(/,/g, ', ').split(/(?=[A-Z])/).map(s => s.toLowerCase()).join(' ') + ')';
          // this.profileUpdateStatus.next(ps);
        },
        errorResponse => {
          this.handleError(errorResponse);
        }
      );
  }

  handleError(httpErrorResponse: HttpErrorResponse) {
    // console.log(httpErrorResponse);
    return throwError(httpErrorResponse);
  }
}
