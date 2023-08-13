// angular
import { Component, OnInit } from '@angular/core'
import { Router, ActivatedRoute } from '@angular/router'

// core and shared
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('error.component')

@Component({
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
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
