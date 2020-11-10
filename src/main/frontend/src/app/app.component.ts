import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { BasicAuthService } from './_services/basic-auth.service';
import { User } from './_models/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  username: string;
  user: User;

  constructor(
    private basicAuthService: BasicAuthService,
    private location: Location) {
      this.basicAuthService.user.subscribe(x => this.user = x);
     }
}
