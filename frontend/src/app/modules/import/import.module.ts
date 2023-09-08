// angular
import { NgModule } from '@angular/core'

// core and shared
import { SharedModule } from '../../shared/shared.module'

// components
import { ImportComponent } from './import.component'
import { ImportRoutingModule } from './import.routing'

@NgModule({
  imports: [SharedModule, ImportRoutingModule, ImportComponent],
  exports: [],
  providers: [],
})
export class ImportModule {}
