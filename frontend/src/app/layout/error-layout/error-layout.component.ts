import { Component } from '@angular/core'
import { RouterOutlet } from '@angular/router'

@Component({
  selector: 'app-error-layout',
  templateUrl: './error-layout.component.html',
  styleUrls: ['./error-layout.component.scss'],
  standalone: true,
  imports: [RouterOutlet],
})
export class ErrorLayoutComponent {}
