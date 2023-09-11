import { NgIf } from '@angular/common'
import { Component } from '@angular/core'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatMenuModule } from '@angular/material/menu'
import { MatToolbarModule } from '@angular/material/toolbar'
import { Router, RouterLink } from '@angular/router'
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
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatToolbarModule,
    NgIf,
    RouterLink,
  ],
})
export class HeadingComponent {
  user: BasicAuthUser

  constructor(
    private basicAuthService: BasicAuthService,
    private router: Router,
    private storageService: StorageService,
  ) {
    //this.storageService.user.subscribe((x) => (this.user = x));
    this.user = this.storageService.getAuthenticatedUser()
  }

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
    this.basicAuthService.signOut('parameter')
  }
}
