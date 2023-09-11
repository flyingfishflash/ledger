import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('error.component')

@Component({
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
  standalone: true,
})
export class ErrorComponent implements OnInit {
  errorState: any

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.errorState = history.state
  }
}
