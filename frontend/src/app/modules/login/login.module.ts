// angular
import { NgModule } from '@angular/core'

// core and shared
import { SharedModule } from '../../shared/shared.module'

// components
import { LoginComponent } from './login.component'
import { LoginRoutingModule } from './login.routing'

@NgModule({
  imports: [SharedModule, LoginRoutingModule, LoginComponent],
  exports: [],
  providers: [],
})
export class LoginModule {}
