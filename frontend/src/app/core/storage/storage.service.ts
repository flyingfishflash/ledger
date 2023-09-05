// angular
import { Injectable } from '@angular/core'

// core and shared
import { BasicAuthUser } from '../../core/authentication/basic-auth-user'
import { Logger } from '../../core/logging/logger.service'

const STORAGE_KEY_AUTHENTICATED_USER = 'authenticated-user'
const STORAGE_KEY_ACTIVE_BOOK_ID = 'active-book-id'
const log = new Logger('storage.service')

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  // constructor() {}

  public saveAuthenticatedUser(user: BasicAuthUser) {
    window.sessionStorage.removeItem(STORAGE_KEY_AUTHENTICATED_USER)
    window.sessionStorage.setItem(
      STORAGE_KEY_AUTHENTICATED_USER,
      JSON.stringify(user),
    )
  }

  public getAuthenticatedUser(): BasicAuthUser {
    const storedValue =
      sessionStorage.getItem(STORAGE_KEY_AUTHENTICATED_USER) ?? null

    if (storedValue !== null) {
      try {
        return JSON.parse(storedValue)
      } catch (err) {
        throw new Error("couldn't parse stored user into BasicAuthUser")
      }
    } else {
      log.debug('no user in session storage')
      return new BasicAuthUser(null)
      //throw new Error('no user in session storage')
    }
  }

  public getLoggedInUserId(): number {
    try {
      return this.getAuthenticatedUser().id
    } catch (err) {
      log.debug("couldn't getLoggedInUserId()")
      throw err
      //this.redirectToLogin();
    }
  }

  public getLoggedInUserName(): string {
    try {
      return this.getAuthenticatedUser().username
    } catch (err) {
      log.debug("couldn't getLoggedInUserName()")
      throw err
      //this.redirectToLogin();
    }
  }

  public saveActiveBookId(bookId: number) {
    window.sessionStorage.removeItem(STORAGE_KEY_ACTIVE_BOOK_ID)
    window.sessionStorage.setItem(STORAGE_KEY_ACTIVE_BOOK_ID, bookId.toString())
  }

  public getActiveBookId(): number {
    try {
      return Number(sessionStorage.getItem(STORAGE_KEY_ACTIVE_BOOK_ID) ?? '')
    } catch (err) {
      log.debug("couldn't retrieve active book id from session storage")
      throw err
    }
  }
}
