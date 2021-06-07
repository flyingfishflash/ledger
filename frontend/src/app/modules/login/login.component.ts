// angular
import { HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";

// third party
import { first } from "rxjs/operators";
import { throwError } from "rxjs";

// core and shared
import { AppConfigRuntime } from "app/app-config-runtime";
import { BasicAuthService } from "@core/authentication/basic-auth.service";
import { Logger } from "@core/logging/logger.service";
import { AppConfigRuntimeInfoBuild } from "app/app-config-runtime-info-build";

const log = new Logger("login.component");

@Component({
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"],
})
export class LoginComponent implements OnInit {
  appConfigInfoBuild: AppConfigRuntimeInfoBuild;
  errorMessage = null;
  form: any = {};
  hide = true;
  isLoggedIn = false;
  isLoginFailed = false;
  isLoginDisabled = true;
  returnUrl: string;

  constructor(
    private basicAuthService: BasicAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private appConfig: AppConfigRuntime
  ) {
    if (this.basicAuthService.userValue) {
      this.router.navigate(["/home"]);
    }
    if (this.appConfig.assets.api.server) {
      if (this.appConfig.api.actuator.info.build) {
        this.appConfigInfoBuild = { ...this.appConfig.api.actuator.info.build };
        this.isLoginDisabled = false;
      } else {
        this.errorMessage =
          "Application build information couldn't be obtained";
      }
    } else {
      this.errorMessage = "API connection not configured";
    }
    this.form.submitted = false;
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || "/";
  }

  onFocusInEvent() {
    this.isLoginFailed = false;
  }

  onSubmit() {
    log.debug("onSubmit");
    this.onSubmitBasicAuth();
  }

  private onSubmitBasicAuth() {
    if (this.form.invalid) {
      return;
    }

    this.basicAuthService
      .signIn(this.form)
      .pipe(first())
      .subscribe(
        (res) => {
          this.isLoggedIn = true;
          this.isLoginFailed = false;
          this.router.navigateByUrl("/home");
          //this.router.navigate([this.returnUrl]);
        },
        (err) => {
          this.handleError(err);
        }
      );
  }

  private handleError(error: any) {
    log.debug(error);
    let errorMessage = null;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`;
    } else if (error instanceof HttpErrorResponse) {
      log.debug("httperror");
      if (error.error.status) {
        log.debug("api error");
        if (error.error.status === "fail") {
          errorMessage = error.error.response.body.message;
        }
      } else {
        log.debug("non-api error");
        errorMessage = `A server-side error occured:\nError Status: ${error.message}`;
      }
    }

    this.errorMessage = errorMessage;
    log.error(errorMessage ? errorMessage : error);
    this.isLoggedIn = false;
    this.isLoginFailed = true;
    return throwError(error);
  }
}
