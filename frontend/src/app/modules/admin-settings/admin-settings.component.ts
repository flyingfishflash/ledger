// angular
import { Component } from "@angular/core";
import { OnInit } from "@angular/core";
import { MatTabChangeEvent } from "@angular/material/tabs";
import { Router } from "@angular/router";

// core and shared
import { UserService } from "@shared/users/user.service";

@Component({
  selector: "app-admin-settings",
  templateUrl: "./admin-settings.component.html",
  styleUrls: ["./admin-settings.component.css"],
})
export class AdminSettingsComponent implements OnInit {
  componentHeading = "Settings";
  users;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.findAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        // this.filteredAccounts = this.accounts;
      },
      //error: err => this.errorMessage = err
    });
  }

  onClickDelete(id: number) {
    if (id) {
      this.userService.userDeleteById(id);
    }
  }

  /*     tabChange($event:MatTabChangeEvent) {
        let selectedTab = $event.tab;
        this.ACTIVE_TAB = selectedTab.textLabel;
      } */
}
