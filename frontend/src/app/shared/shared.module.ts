// modules (angular)
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FlexLayoutModule } from "@angular/flex-layout";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

// modules
import { MaterialModule } from "app/material.module";

// services
import { DirtyCheckService } from "@shared/dirty-check/dirty-check.service";
import { EqualObjectsService } from "@shared/equal-objects/equal-objects.service";
import { ErrorDialogService } from "@shared/errors/error-dialog.service";
import { TreeUtilitiesService } from "@shared/tree-utilities/tree-utilties.service";
import { UserService } from "@shared/users/user.service";
import { UtilitiesService } from "@shared/utilities/utilities.service";

// components
import { ErrorDialogComponent } from "./errors/error-dialog/error-dialog.component";

// pipes
import { PadWithSpacesPipe } from "@shared/pipes/pad-with-spaces.pipe";

const sharedComponents = [ErrorDialogComponent, PadWithSpacesPipe];

@NgModule({
  declarations: [...sharedComponents],
  exports: [
    sharedComponents,
    CommonModule,
    FlexLayoutModule,
    FormsModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  imports: [
    CommonModule,
    FlexLayoutModule,
    FormsModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  providers: [
    DirtyCheckService,
    EqualObjectsService,
    ErrorDialogService,
    TreeUtilitiesService,
    UserService,
    UtilitiesService,
  ],
})
export class SharedModule {}
