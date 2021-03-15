// angular
import { Location } from "@angular/common";
import { Component, OnInit } from "@angular/core";

// core and shared
import { environment } from "../environments/environment";
import { BasicAuthService } from "@core/authentication/basic-auth.service";
import { BasicAuthUser } from "@core/authentication/basic-auth-user";
import { Logger } from "@core/logging/logger.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit {
  username: string;
  user: BasicAuthUser;

  constructor(
    private basicAuthService: BasicAuthService,
    private location: Location
  ) {
    this.basicAuthService.user.subscribe((x) => (this.user = x));
  }

  ngOnInit(): void {
    if (environment.production) {
      Logger.enableProductionMode();
    }
  }
}
