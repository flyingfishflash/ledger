// angular
import { Component, OnInit } from '@angular/core'
import { MatTabsModule } from '@angular/material/tabs'
import { Router, RouterLink } from '@angular/router'

// core and shared
import { NgFor } from '@angular/common'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatLineModule } from '@angular/material/core'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatListModule } from '@angular/material/list'
import { MatTooltipModule } from '@angular/material/tooltip'
import { UserService } from '../../shared/users/user.service'

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings.component.html',
  styleUrls: ['./admin-settings.component.css'],
  standalone: true,
  imports: [
    MatCardModule,
    MatTabsModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatTooltipModule,
    RouterLink,
    MatListModule,
    NgFor,
    MatLineModule,
  ],
})
export class AdminSettingsComponent implements OnInit {
  componentHeading = 'Settings'
  users: any

  constructor(
    private userService: UserService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.userService.findAllUsers().subscribe({
      next: (users) => {
        this.users = users
        // this.filteredAccounts = this.accounts;
      },
      //error: err => this.errorMessage = err
    })
  }

  onClickDelete(id: number) {
    if (id) {
      this.userService.userDeleteById(id)
    }
  }

  /*     tabChange($event:MatTabChangeEvent) {
        let selectedTab = $event.tab;
        this.ACTIVE_TAB = selectedTab.textLabel;
      } */
}
