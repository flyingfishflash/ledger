import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subject, timer, throwError } from 'rxjs';
import { map, switchMap, retry, tap, share, takeUntil, catchError } from 'rxjs/operators';

const API = environment.api.url;


@Injectable({
  providedIn: 'root'
})
export class ImportService {

  private allCurrencies$: Observable<any[]>;

  private stopPolling = new Subject();

  constructor(
    private http: HttpClient,
  ) {

    console.log('constructing import service');

    this.allCurrencies$ = timer(1, 1000).pipe(
      switchMap(() => http.get<any[]>(`${API}/import/gnucashFileImportStatus`)),
      retry(),
      tap(console.log),
      share(),
      takeUntil(this.stopPolling)
    );

    console.log(this.allCurrencies$);
  }

  getImportStatus(): Observable<any> {
    console.log('getImportStatus()');
    return this.http.get<any[]>(`${API}/import/gnucashFileImportStatus`).pipe(
      map(res => res), catchError(this.handleError)
    );
  }

  getAllCurrencies(): Observable<any[]> {
    console.log('in getAllCurrencies');
    return this.allCurrencies$.pipe(
      tap(() => console.log('data sent to subscriber!!!'))
    );
  }

  uploadFile(formData): Observable<any> {
    return this.http.post<any[]>(`${API}/import/gnucash`, formData).pipe(
      map(res => {
        return res;
      }
      ));
  }

  handleError(err: HttpErrorResponse) {
    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      errorMessage = 'An error occurred: ' + err.error.message;
    } else {
      errorMessage = 'HttpErrorResponse: ' + err.status + ' / ' + err.message;
    }
    console.log('Error handled.');
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
