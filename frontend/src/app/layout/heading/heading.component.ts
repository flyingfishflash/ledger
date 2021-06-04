// angular
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

// core and shared
import { BasicAuthService } from "@core/authentication/basic-auth.service";
import { BasicAuthUser } from "@core/authentication/basic-auth-user";
import { BasicAuthUserRole } from "@core/authentication/basic-auth-user-role";
import { Logger } from "@core/logging/logger.service";
import { StorageService } from "@core/storage/storage.service";

const log = new Logger("heading.component");

@Component({
  selector: "app-heading",
  templateUrl: "./heading.component.html",
  styleUrls: ["./heading.component.css"],
})
export class HeadingComponent implements OnInit {
  isLoggedIn = false;
  user: BasicAuthUser;

  constructor(
    private authenticationService: BasicAuthService,
    private storageService: StorageService,
    private router: Router
  ) {
    //this.storageService.user.subscribe((x) => (this.user = x));
    this.user = this.storageService.getAuthenticatedUser();
  }

  ngOnInit(): void {}

  navigateToProfile() {
    // if already at the profile route, force a reload of window, which will refresh the
    // component using the currently logged in user's data
    if (this.router.url === "/profile") {
      location.reload();
    } else {
      this.router.navigateByUrl("/profile", {
        state: { data: { userId: this.user.id } },
      });
    }
  }

  get isAdmin() {
    return (
      this.user && this.user.roles.includes(BasicAuthUserRole.administrator)
    );
  }

  logout() {
    this.authenticationService.signOut("parameter");
  }
}
