import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings.component.html',
  styleUrls: ['./admin-settings.component.css']
})
export class AdminSettingsComponent implements OnInit {

  componentHeading = 'Settings';
  users;

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.userService.findAllUsers().subscribe({
      next: users => {
        this.users = users;
        // console.log(this.users);
        // console.log(this.users.authorities[0].authority);
        // this.filteredAccounts = this.accounts;
      },
      //error: err => this.errorMessage = err
    });
  }

  onClickDelete(id: number) {
    console.log('onClickDelete')
    if (id) {
      this.userService.userDeleteById(id);
    }

  }

  /*     tabChange($event:MatTabChangeEvent) {
        let selectedTab = $event.tab;
        this.ACTIVE_TAB = selectedTab.textLabel;
      } */

}
