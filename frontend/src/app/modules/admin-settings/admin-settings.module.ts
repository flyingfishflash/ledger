// modules (angular)
import { NgModule } from '@angular/core';

// core and shared
import { SharedModule } from '../../shared/shared.module';

// components
import { AdminSettingsComponent } from './admin-settings.component';

import { AdminSettingsRoutingModule } from './admin-settings.routing';

@NgModule({
  declarations: [AdminSettingsComponent],
  imports: [SharedModule, AdminSettingsRoutingModule],
  exports: [],
  providers: [],
})
export class AdminSettingsModule {}
