import { Component, OnInit } from '@angular/core';
import { BasicAuthService } from '../_services/basic-auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/internal/operators/first';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  hide = true;
  returnUrl: string;

  constructor(
    private basicAuthService: BasicAuthService,
    private router: Router,
    private route: ActivatedRoute,
  ) {

    // redirect to home if already logged in
    if (this.basicAuthService.userValue) {
      this.router.navigate(['/home']);
    }
  }

  ngOnInit() {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit() {
    this.onSubmitBasicAuth();
  }

  private onSubmitBasicAuth() {

    // this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    // this.loading = true;
    this.basicAuthService.signIn(this.form)
      .pipe(first())
      .subscribe(
        data => {
          console.log(data);
          this.isLoggedIn = true;
          this.isLoginFailed = false;
          this.router.navigate([this.returnUrl]);
        },
        error => {
          // this.error = error;
          // this.loading = false;
        });
  }

/*   private onSubmitBasicAuth1() {
    this.basicAuthService.signIn(this.form).subscribe(
      (result) => {
        console.log('result below:');
        console.log(result);
        if (result.name) {
          this.isLoggedIn = true;
          this.isLoginFailed = false;
          //this.basicAuthService.registerSuccessfulLogin(this.form.username, this.form.password);
          this.basicAuthService.registerSuccessfulLogin1(result.principal, this.form.password);
          console.log(this.basicAuthService.userValue);
          //setTimeout(() => {  console.log("World!"); }, 2000);
          this.router.navigate([this.returnUrl]);
        } else {
          this.isLoginFailed = true;
          this.isLoggedIn = false;
        }
      },
      err => {
        // err.error.message is populated in the case of a JSON response
        console.log('there was an error');
        console.log(err);
        this.errorMessage = err.error.exception;
        console.log(this.errorMessage);
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
      console.log(this.userDetails);
    } */
}
