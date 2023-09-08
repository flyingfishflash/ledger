// angular
import { NgModule } from '@angular/core'

// core and shared
import { SharedModule } from '../../shared/shared.module'

// components
import { ProfileComponent } from './profile.component'
import { ProfileRoutingModule } from './profile.routing'

@NgModule({
  imports: [SharedModule, ProfileRoutingModule, ProfileComponent],
  exports: [],
  providers: [],
})
export class ProfileModule {}
