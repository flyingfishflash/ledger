import { NgIf } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import { HeadingComponent } from '../../core/heading/heading.component'
import { StorageService } from '../../core/storage/storage.service'

@Component({
  selector: 'app-content-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  standalone: true,
  imports: [HeadingComponent, RouterOutlet, NgIf],
})
export class LayoutComponent implements OnInit {
  constructor(private storageService: StorageService) {}

  isAuthenticated(): boolean {
    return this.storageService.getAuthenticatedUser().id !== 0
  }

  ngOnInit(): void {
    console.log('user: ' + this.storageService.getAuthenticatedUser().id)
  }
  // }
}
