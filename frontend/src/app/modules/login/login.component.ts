// angular
import { HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { MatIconRegistry } from "@angular/material/icon";
import { DomSanitizer } from "@angular/platform-browser";

// third party
import { first } from "rxjs/operators";
import { throwError } from "rxjs";

// core and shared
import { environment } from "@env";
import { AppConfigRuntime } from "app/app-config-runtime";
import { AppConfigRuntimeInfoBuild } from "app/app-config-runtime-info-build";
import { BasicAuthService } from "@core/authentication/basic-auth.service";
import { Logger } from "@core/logging/logger.service";

const log = new Logger("login.component");

@Component({
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  appConfigInfoBuild: AppConfigRuntimeInfoBuild;
  errorMessage = null;
  form: any = {};
  isPasswordHidden = true;
  isLoggedIn = false;
  isLoginFailed = false;
  isLoginDisabled = true;
  isLoginViaBackend = true;
  returnUrl: string;

  constructor(
    private basicAuthService: BasicAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private appConfig: AppConfigRuntime,
    private iconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
    if (this.basicAuthService.userValue) {
      this.router.navigate(["/home"]);
    }
    if (environment.api.server) {
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
    iconRegistry.addSvgIcon(
      "sso-zitadel",
      this.domSanitizer.bypassSecurityTrustResourceUrl(
        "../../../assets/images/zitadel-logo-solo-dark.svg"
      )
    );
    iconRegistry.addSvgIcon(
      "sso-github",
      this.domSanitizer.bypassSecurityTrustResourceUrl(
        "../../../assets/images/github-mark.svg"
      )
    );
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
      .subscribe({
        next: () => {
          this.isLoggedIn = true;
          this.isLoginFailed = false;
          this.router.navigateByUrl("/home");
          //this.router.navigate([this.returnUrl]);
        },
        error: (err) => {
          this.handleError(err);
        },
      });
  }

  private handleError(error: any) {
    log.debug(error);
    let errorMessage = null;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.detail}`;
    } else if (error instanceof HttpErrorResponse) {
      log.debug("httperror");
      if (error.error.disposition) {
        log.debug("api error");
        if (error.error.disposition === "failure") {
          errorMessage = error.error.content.detail;
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
    throwError(() => error);
  }
}
