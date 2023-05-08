// modules (angular)
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

// modules
import { MaterialModule } from "app/material.module";

// services
import { ActuatorService } from "@shared/actuator/actuator.service";
import { BookService } from "@shared/books/book.service";
import { DirtyCheckService } from "@shared/dirty-check/dirty-check.service";
import { EqualObjectsService } from "@shared/equal-objects/equal-objects.service";
import { ErrorDialogService } from "@shared/errors/error-dialog.service";
import { TreeUtilitiesService } from "@shared/tree-utilities/tree-utilties.service";
import { UserService } from "@shared/users/user.service";
import { UtilitiesService } from "@shared/utilities/utilities.service";

import { PadWithSpacesPipe } from "@shared/pipes/pad-with-spaces.pipe";

// components
import { ErrorDialogComponent } from "./errors/error-dialog/error-dialog.component";

const sharedComponents = [ErrorDialogComponent, PadWithSpacesPipe];

@NgModule({
  declarations: [...sharedComponents],
  exports: [
    sharedComponents,
    CommonModule,
    FormsModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  imports: [
    CommonModule,
    FormsModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  providers: [
    ActuatorService,
    BookService,
    DirtyCheckService,
    EqualObjectsService,
    ErrorDialogService,
    TreeUtilitiesService,
    UserService,
    UtilitiesService,
  ],
})
export class SharedModule {}
