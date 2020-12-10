// angular
import { Injectable } from "@angular/core";

// core and shared
import { BasicAuthUser } from "@core/authentication/basic-auth-user";
import { Logger } from "@core/logging/logger.service";

const STORAGE_KEY_AUTHENTICATED_USER = "authenticated-user";
const log = new Logger("storage.service");

@Injectable({
  providedIn: "root",
})
export class StorageService {
  constructor() {}

  public saveAuthenticatedUser(user: BasicAuthUser) {
    window.sessionStorage.removeItem(STORAGE_KEY_AUTHENTICATED_USER);
    window.sessionStorage.setItem(
      STORAGE_KEY_AUTHENTICATED_USER,
      JSON.stringify(user)
    );
  }

  public getAuthenticatedUser(): BasicAuthUser {
    try {
      return JSON.parse(sessionStorage.getItem(STORAGE_KEY_AUTHENTICATED_USER));
    } catch (err) {
      log.debug("couldn't retrieve authenticated user from session storage");
      //this.redirectToLogin();
    }
  }

  public getLoggedInUserId() {
    try {
      const id = this.getAuthenticatedUser().id;
      if (id === null) {
        return null;
      }
      return id;
    } catch (err) {
      //this.redirectToLogin();
    }
  }

  public getLoggedInUserName() {
    try {
      const userName = this.getAuthenticatedUser().username;
      if (userName === null) {
        return null;
      }
      return userName;
    } catch (err) {
      //this.redirectToLogin();
    }
  }
}
