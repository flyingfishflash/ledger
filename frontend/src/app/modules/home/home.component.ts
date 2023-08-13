// angular
import { Component, OnInit } from '@angular/core'

// core and shared
import { UserService } from '../../shared/users/user.service'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  content: string

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.content = 'Home Component'
  }
}
