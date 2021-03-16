// angular
import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";

// third party
import { first } from "rxjs/internal/operators/first";

// core and shared
import { ActuatorInfo } from "@shared/actuator/actuator-info";
import { ActuatorService } from "@shared/actuator/actuator.service";
import { BasicAuthService } from "@core/authentication/basic-auth.service";

@Component({
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"],
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = "";
  hide = true;
  returnUrl: string;
  info: ActuatorInfo;
  buildDate: string;
  todayString: string;

  constructor(
    private basicAuthService: BasicAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private actuatorService: ActuatorService
  ) {
    // redirect to home if already logged in
    if (this.basicAuthService.userValue) {
      this.router.navigate(["/home"]);
    }
  }

  ngOnInit() {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || "/";

    this.actuatorService.getInfo().subscribe({
      next: (response) => {
        this.info = response;
        this.buildDate = response.build.time;
        //this.todayString = new Date(info.build.time).toUTCString;
      },
      error: (err) => (this.errorMessage = err),
    });

    //this.init();
  }

  /*   init() {
    this.todayString = new Date(this.buildDate.toString()).toUTCString;
  }
 */
  onSubmit() {
    this.onSubmitBasicAuth();
  }

  private onSubmitBasicAuth() {
    // this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    // this.loading = true;
    this.basicAuthService
      .signIn(this.form)
      .pipe(first())
      .subscribe(
        (data) => {
          //log.debug(data);
          this.isLoggedIn = true;
          this.isLoginFailed = false;
          this.router.navigateByUrl("/home");
          //this.router.navigate([this.returnUrl]);
        },
        (error) => {
          // this.error = error;
          // this.loading = false;
        }
      );
  }

  /*   private onSubmitBasicAuth1() {
    this.basicAuthService.signIn(this.form).subscribe(
      (result) => {
        log.debug('result below:');
        log.debug(result);
        if (result.name) {
          this.isLoggedIn = true;
          this.isLoginFailed = false;
          //this.basicAuthService.registerSuccessfulLogin(this.form.username, this.form.password);
          this.basicAuthService.registerSuccessfulLogin1(result.principal, this.form.password);
          log.debug(this.basicAuthService.userValue);
          //setTimeout(() => {  log.debug("World!"); }, 2000);
          this.router.navigate([this.returnUrl]);
        } else {
          this.isLoginFailed = true;
          this.isLoggedIn = false;
        }
      },
      err => {
        // err.error.message is populated in the case of a JSON response
        log.debug('there was an error');
        log.debug(err);
        this.errorMessage = err.error.exception;
        log.debug(this.errorMessage);
        this.isLoginFailed = true;
        this.isLoggedIn = false;
      }
    );
  } */

  /*   private onSubmitJwtTokenAuth() {
      this.authService.login(this.form).subscribe(
        data => {
          // this.tokenStorage.saveToken(data.accessToken);
          // this.tokenStorage.saveUser(data);
  
          this.isLoginFailed = false;
          this.isLoggedIn = true;
          // this.roles = this.tokenStorage.getUser().roles;
          this.reloadPage();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isLoginFailed = true;
        }
      );
    }
   */
  /*   private reloadPage() {
      window.location.reload();
      // this.basicAuthService.getUserDetails();
  
      this.basicAuthService.getUserDetails().subscribe({
        next: response => {
          this.userDetails = response;
        },
        error: err => this.errorMessage = err
      });
      log.debug(this.userDetails);
    } */
}