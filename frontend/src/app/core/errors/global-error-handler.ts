// angular
import { ErrorHandler, Injectable, NgZone } from '@angular/core'
//import { Router } from '@angular/router';

import { Logger } from '../../core/logging/logger.service'
import { ErrorDialogService } from '../../shared/errors/error-dialog.service'

const log = new Logger('global-error-handler')

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(
    private zone: NgZone,
    private errorDialogService: ErrorDialogService,
  ) {} // private router: Router, // private errorDialogService: ErrorDialogService,

  handleError(error: Error) {
    /* 
    this.zone.run(() =>
    this.errorDialogService.openDialog(
    error.message || "Undefined client error"
    ));
    */

    //this.router.navigate(["/error"]);
    log.debug('global error handler triggered:')
    log.debug(error)
    log.error(`\nError Message: ${error.message}`)
    this.zone.run(() =>
      this.errorDialogService.openDialog(
        error.message || 'Undefined client error',
      ),
    )
    //console.error(error);
  }
}
