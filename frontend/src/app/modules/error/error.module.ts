// angular
import { NgModule } from '@angular/core'

// core and shared
import { SharedModule } from '../../shared/shared.module'

// components
import { ErrorComponent } from './error.component'
import { ErrorRoutingModule } from './error.routing'

@NgModule({
  imports: [SharedModule, ErrorRoutingModule, ErrorComponent],
  exports: [],
  providers: [],
})
export class ErrorModule {}
