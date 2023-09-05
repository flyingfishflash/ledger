// angular
import { HttpErrorResponse } from '@angular/common/http'
import { Component, OnInit } from '@angular/core'
import { Router, ActivatedRoute } from '@angular/router'
import { MatIconRegistry } from '@angular/material/icon'
import { DomSanitizer } from '@angular/platform-browser'

// third party
import { first } from 'rxjs/operators'
import { throwError } from 'rxjs'

// core and shared
import { ActuatorService } from '../../shared/actuator/actuator.service'
import { AppConfigRuntime } from '../../../app/app-config-runtime'
import { BuildProperties } from '../../../app/app-build-properties'
import { BasicAuthService } from '../../core/authentication/basic-auth.service'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('login.component')

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  buildProperties: BuildProperties
  errorMessage = ''
  form: any = {}
  isPasswordHidden = true
  isLoggedIn = false
  isLoginFailed = false
  isLoginDisabled = true
  isLoginViaBackend = true
  returnUrl: string

  constructor(
    private actuatorService: ActuatorService,
    private basicAuthService: BasicAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private appConfig: AppConfigRuntime,
    private iconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
  ) {
    if (this.basicAuthService.userValue.id !== 0) {
      log.debug('user id !== 0' + basicAuthService.userValue.id)
      this.router.navigate(['/home'])
    }

    if (this.appConfig.buildProperties) {
      this.buildProperties = { ...this.appConfig.buildProperties }
    } else {
      log.error('Build properties are not populated')
    }

    this.form.submitted = false
    iconRegistry.addSvgIcon(
      'sso-zitadel',
      this.domSanitizer.bypassSecurityTrustResourceUrl(
        '../../../assets/images/zitadel-logo-solo-dark.svg',
      ),
    )

    iconRegistry.addSvgIcon(
      'sso-github',
      this.domSanitizer.bypassSecurityTrustResourceUrl(
        '../../../assets/images/github-mark.svg',
      ),
    )
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/'

    this.actuatorService.getHealthStatusSimple().subscribe({
      next: (healthStatusSimple) => {
        if (healthStatusSimple) {
          log.debug('Api server health status is UP')
          this.isLoginDisabled = false
        } else {
          log.debug('Api server health status is not UP')
          this.errorMessage = 'Api server status is not UP'
        }
      },
      error: (error) => {
        log.debug('health subscription' + error)
        this.handleError(error)
      },
    })
  }

  onFocusInEvent() {
    this.isLoginFailed = false
  }

  onSubmit() {
    log.debug('onSubmit')
    this.onSubmitBasicAuth()
  }

  private onSubmitBasicAuth() {
    if (this.form.invalid) {
      return
    }

    this.basicAuthService
      .signIn(this.form)
      .pipe(first())
      .subscribe({
        next: () => {
          this.isLoggedIn = true
          this.isLoginFailed = false
          this.router.navigateByUrl('/home')
          //this.router.navigate([this.returnUrl]);
        },
        error: (err) => {
          this.handleError(err)
        },
      })
  }

  private handleError(error: any) {
    log.debug(error)
    let errorMessage = ''
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.detail}`
    } else if (error instanceof HttpErrorResponse) {
      log.debug('httperror')
      const errorUrl = error.url ?? ''
      if (error.error.disposition) {
        log.debug('api error')
        if (error.error.disposition === 'failure') {
          if (errorUrl.includes('/health')) {
            errorMessage = `Api healthcheck failed: ${error.error.content.title}`
          } else {
            errorMessage = error.error.content.detail
          }
        }
      } else {
        log.debug('non-api error')
        const errorUrl = error.url ?? ''
        log.error(
          `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`,
        )
        if (errorUrl.includes('/health')) {
          errorMessage = 'Api server could not be reached. Is it running?'
        }
      }
    }

    this.errorMessage = errorMessage
    log.error(errorMessage ? errorMessage : error)
    this.isLoggedIn = false
    this.isLoginFailed = true
    throwError(() => error)
  }
}
