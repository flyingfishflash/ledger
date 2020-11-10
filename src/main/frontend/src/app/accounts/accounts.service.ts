import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { list_to_tree_sorted } from 'src/app/_helpers/tree-utilities';
import { IAccount } from './account';

const API = environment.api.url;

@Injectable({
    providedIn: 'root'
})
export class AccountsService {

    // private accountUrl = '/api/accounts/accounts.json';

    constructor(private http: HttpClient) { }

    getAccounts(): Observable<any> {
        return this.http.get<any>(`${API}/accounts`).pipe(
            map(res => {
                return res.response.body;
            }), catchError(this.handleError)
        );
    }

    /*
         getAccounts(): Observable<any> {
             return this.http.get<IAccount[]>(`${API}/accounts`).pipe(
                 map(res => res), catchError(this.handleError)
             );
         }
    */

    getAccountsTree(): Observable<any> {
        return this.http.get<any>(`${API}/accounts`).pipe(
            map(res => {
                return list_to_tree_sorted(res.response.body);
            }), catchError(this.handleError)
        );
    }

    /*
         getAccountsTree(): Observable<any> {
             return this.http.get<IAccount[]>(`${API}/accounts`).pipe(
                 map(res => list_to_tree_sorted(res))
            );
        }
     */

    getAccountCategories(): Observable<string[]> {
        return this.http.get<any>(`${API}/account-categories`).pipe(
            map(res => {
                return res.response.body;
            }), catchError(this.handleError)
        );
    }

    /*
         getAccountCategories(): Observable<string[]> {
             return this.http.get<string[]>(`${API}/account-categories`).pipe(
                map(res => res), catchError(this.handleError)
            );
        }
    */

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
