import { environment } from 'src/environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, from, throwError } from 'rxjs';
import { map } from 'rxjs/operators';

import { IAccount } from '../accounts-table/account';

const API = environment.api.url;

@Injectable({
    // available for injection by angular from anywhere in application
    providedIn: 'root'
})
export class AccountsTreeService {

    // private accountUrl = '/api/accounts/accounts.json';
    private accountUrl = 'http://nas:8181/api/v1/ledger/accounts';

    constructor(private http: HttpClient) {}

    list_to_tree(list) {

        // let map = {};
        let node;
        let roots = [];
        let i;
        let minId = 0;
        let maxId = 0;
        let minParentId = 0;

        /*
            Get min id, max id, min parent id
            The minumum parent id will be considered the root account
        */

        minId = list[0].id;
        maxId = list[0].id;
        minParentId = list[0].parentId;

        for (i = 0; i < list.length; i += 1) {
            if (list[i].id < minId) { minId = list[i].id; }
            if (list[i].id > maxId) { maxId = list[i].id; }
            if (list[i].parentId < minParentId) { minParentId = list[i].parentId; }
        }
        console.log('minId: ' + minId);
        console.log('maxId: ' + maxId);
        console.log('minParentId: ' + minParentId);

        for (i = 0; i <= maxId - minId; i += 1) {
            map[list[i].id] = i; // initialize the map
            list[i].children = []; // initialize the children
        }

        for (i = 0; i <= maxId - minId; i += 1) {
            node = list[i];

            if (node.parentId !== minParentId) {
                // if you have dangling branches check that map[node.parentId] exists
                list[map[node.parentId]].children.push(node);
            } else {
                roots.push(node);
            }
        }
        // console.log(roots);
        return roots;
    }

    unflatten(list) {

        let i = 0;
        let tree = [];
        let mappedArr = {};
        let arrElem;
        let mappedElem;
        let minId = 0;
        let maxId = 0;
        let minParentId = 0;

            /*
                Get min id, max id, min parent id
                The minumum parent id will be considered the root account
            */

        minId = list[0].id;
        maxId = list[0].id;
        minParentId = list[0].parentId;

        for (i = 0; i < list.length; i += 1) {
            if (list[i].id < minId) { minId = list[i].id; }
            if (list[i].id > maxId) { maxId = list[i].id; }
            if (list[i].parentId < minParentId) { minParentId = list[i].parentId; }
        }

        console.log('minId: ' + minId);
        console.log('maxId: ' + maxId);
        console.log('minParentId: ' + minParentId);

        // First map the nodes of the array to an object -> create a hash table.
        for (i = 0; i <= maxId - minId;  i++) {
            arrElem = list[i];
            mappedArr[arrElem.id] = arrElem;
            mappedArr[arrElem.id]['children'] = [];
        }

        // console.log(mappedArr);

        for (let id in mappedArr) {
          if (mappedArr.hasOwnProperty(id)) {
            mappedElem = mappedArr[id];
            // If the element is not at the root level, add it to its parent array of children.
            if (mappedElem.parentId !== 27) {
              mappedArr[mappedElem['parentId']]['children'].push(mappedElem);
            } else {
                // If the element is at the root level, add it to first level elements array.
                tree.push(mappedElem);
            }
          }
        }
        // console.log(tree);
        return tree;
      }


      insertAsNextSibling() {
      // http://localhost:8181/ledger/accounts/insertAsNextSibling?id=33
      // http://localhost:8181/ledger/api/v1/accounts/insert-as-next-sibling?id=33
          
      }

//    getAccounts(): Observable<IAccount[]> {

      getAccounts(paramData): Observable<any> {

//        return this.http.get<IAccount[]>(this.accountUrl).pipe(
        return this.http.get<IAccount[]>(`${API}/accounts`, {params: paramData}).pipe(

            map(res => { return this.list_to_tree(res); }));

/*
        return this.http.get<IAccount[]>(this.accountUrl).pipe(
            tap(data => console.log('accounts-tree-service: ' + (JSON.stringify(this.list_to_tree(data))))),
            catchError(this.handleError)
        ); */
    }

    handleError(err: HttpErrorResponse) {
        let errorMessage = '';
        if (err.error instanceof ErrorEvent) {
            errorMessage = 'An error occurred: ${err.error.message)';
        } else {
            errorMessage = 'Server returned code:' + err.status + ' ' + err.message;
        }
        console.log('something is wrong');
        console.error(errorMessage);
        return throwError(errorMessage);
    }
}
