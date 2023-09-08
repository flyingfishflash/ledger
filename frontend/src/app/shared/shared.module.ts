// modules (angular)
import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { RouterModule } from '@angular/router'

// modules

// services
import { ActuatorService } from '../shared/actuator/actuator.service'
import { BookService } from '../shared/books/book.service'
import { ErrorDialogService } from '../shared/errors/error-dialog.service'
import { UserService } from '../shared/users/user.service'
import { UtilitiesService } from '../shared/utilities/utilities.service'

import { PadWithSpacesPipe } from '../shared/pipes/pad-with-spaces.pipe'

// components
import { ErrorDialogComponent } from './errors/error-dialog/error-dialog.component'

const sharedComponents = [ErrorDialogComponent, PadWithSpacesPipe]

@NgModule({
  exports: [
    sharedComponents,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    ...sharedComponents,
  ],
  providers: [
    ActuatorService,
    BookService,
    ErrorDialogService,
    UserService,
    UtilitiesService,
  ],
})
export class SharedModule {}
