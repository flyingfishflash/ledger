import { Component } from '@angular/core'

@Component({
  selector: 'app-home',
  standalone: true,
  styleUrls: ['./home.component.css'],
  templateUrl: './home.component.html',
})
export class HomeComponent {
  content: string = 'Home Component'

  constructor() {}
}
