// angular
import { Location } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { Router, RouterOutlet } from '@angular/router'

// core and shared
import { environment } from '../environments/environment'
import { BasicAuthUser } from './core/authentication/basic-auth-user'
import { BasicAuthService } from './core/authentication/basic-auth.service'
import { Logger } from './core/logging/logger.service'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true,
  imports: [RouterOutlet],
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
