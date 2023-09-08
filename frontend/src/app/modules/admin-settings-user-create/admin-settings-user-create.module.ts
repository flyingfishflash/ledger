// angular
import { NgModule } from '@angular/core'

// core and shared
import { SharedModule } from '../../shared/shared.module'

// components
import { AdminSettingsUserCreateComponent } from './admin-settings-user-create.component'

import { AdminSettingsUserCreateRoutingModule } from './admin-settings-user-create.routing'

@NgModule({
  imports: [
    SharedModule,
    AdminSettingsUserCreateRoutingModule,
    AdminSettingsUserCreateComponent,
  ],
  exports: [],
  providers: [],
})
export class AdminSettingsUserCreateModule {}
