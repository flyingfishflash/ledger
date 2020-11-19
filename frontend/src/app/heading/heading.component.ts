import { Component, OnInit } from '@angular/core';
import { BasicAuthService } from '../_services/basic-auth.service';
import { User } from '../_models/user';
import { Role } from '../_models/role';
import { Router } from '@angular/router';

@Component({
  selector: 'app-heading',
  templateUrl: './heading.component.html',
  styleUrls: ['./heading.component.css']
})
export class HeadingComponent implements OnInit {
  isLoggedIn = false;
  username: string;
  user: User;

  constructor(
    private basicAuthService: BasicAuthService,
    private router: Router
  ) {
    this.basicAuthService.user.subscribe(x => this.user = x);
    this.username = this.user.username;
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.basicAuthService.isUserLoggedIn();
    if (this.isLoggedIn) {
      this.username = this.basicAuthService.getLoggedInUserName();
    }
  }

  navigateToProfile() {
    // if already at the profile route, force a reload of window, which will refresh the 
    // component using the currently logged in user's data
    if (this.router.url == '/profile') {
      location.reload();
    } else {
      this.router.navigateByUrl('/profile', { state: { data: { userId: this.user.id } } });
    }
  }

  get isAdmin() {
    return this.user && this.user.roles.includes(Role.Administrator);
  }

  logout() {
    this.basicAuthService.signOut('parameter');
  }
}
