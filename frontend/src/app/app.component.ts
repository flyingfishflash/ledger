// angular
import { Location } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { Router } from '@angular/router'

// core and shared
import { BasicAuthService } from './core/authentication/basic-auth.service'
import { BasicAuthUser } from './core/authentication/basic-auth-user'
import { Logger } from './core/logging/logger.service'
import { environment } from '../environments/environment'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  username: string = ''
  user = new BasicAuthUser(null)

  constructor(
    private basicAuthService: BasicAuthService,
    private location: Location,
    private router: Router,
  ) {
    this.basicAuthService.user.subscribe((x) => {
      this.user = x
      console.log(this.user)
      if (this.user.id === 0) {
        this.router.navigate(['/login'])
      }
    })
  }

  ngOnInit(): void {
    if (environment.production) {
      Logger.enableProductionMode()
    }
  }
}
