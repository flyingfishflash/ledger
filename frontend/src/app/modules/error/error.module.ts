// angular
import { NgModule } from '@angular/core';

// core and shared
import { SharedModule } from '@shared/shared.module';

// components
import { ErrorComponent } from './error.component';
import { ErrorRoutingModule } from './error.routing';

@NgModule({
  declarations: [ErrorComponent],
  imports: [SharedModule, ErrorRoutingModule],
  exports: [],
  providers: [],
})
export class ErrorModule {}
