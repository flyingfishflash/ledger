// angular
import { Component } from '@angular/core'
import { Router, RouterLink } from '@angular/router'

// core and shared
import { NgIf } from '@angular/common'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatMenuModule } from '@angular/material/menu'
import { MatToolbarModule } from '@angular/material/toolbar'
import { BasicAuthUser } from '../../core/authentication/basic-auth-user'
import { BasicAuthUserRole } from '../../core/authentication/basic-auth-user-role'
import { BasicAuthService } from '../../core/authentication/basic-auth.service'
import { Logger } from '../../core/logging/logger.service'
import { StorageService } from '../../core/storage/storage.service'

const log = new Logger('heading.component')

@Component({
  selector: 'app-heading',
  templateUrl: './heading.component.html',
  styleUrls: ['./heading.component.css'],
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    RouterLink,
    MatMenuModule,
    NgIf,
    MatIconModule,
  ],
})
export class HeadingComponent {
  isLoggedIn = false
  user: BasicAuthUser

  constructor(
    private authenticationService: BasicAuthService,
    private storageService: StorageService,
    private router: Router,
  ) {
    //this.storageService.user.subscribe((x) => (this.user = x));
    this.user = this.storageService.getAuthenticatedUser()
  }

  // ngOnInit(): void {}

  get isAdmin() {
    return (
      this.user && this.user.roles.includes(BasicAuthUserRole.administrator)
    )
  }

  navigateToProfile() {
    // if already at the profile route, force a reload of window, which will refresh the
    // component using the currently logged in user's data
    if (this.router.url === '/profile') {
      location.reload()
    } else {
      this.router.navigateByUrl('/profile', {
        state: { data: { userId: this.user.id } },
      })
    }
  }

  logout() {
    this.authenticationService.signOut('parameter')
  }
}
