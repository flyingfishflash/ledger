// angular
import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";

// third party
import { Observable, throwError } from "rxjs";
import { catchError, map } from "rxjs/operators";

// core and shared
import { AppConfigRuntime } from "../../app-config-runtime";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("books.service");

@Injectable({
  providedIn: "root",
})
export class BookService {
  constructor(private appConfig: AppConfigRuntime, private http: HttpClient) {}

  postBook(bookName: string): Observable<any> {
    return this.http
      .post<any>(this.appConfig.assets.api.server.url + "/books/", {
        name: bookName,
      })
      .pipe(catchError(this.handleError));
  }

  handleError(httpErrorResponse: HttpErrorResponse) {
    return throwError(httpErrorResponse);
  }
}
