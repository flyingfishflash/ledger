// angular
import { NgModule } from "@angular/core";

// components
import { LoginComponent } from "./login.component";
import { LoginRoutingModule } from "./login.routing";

// core and shared
import { SharedModule } from "@shared/shared.module";

@NgModule({
  declarations: [LoginComponent],
  imports: [SharedModule, LoginRoutingModule],
  exports: [],
  providers: [],
})
export class LoginModule {}
