// angular
import { NgModule } from "@angular/core";

// core and shared
import { SharedModule } from "@shared/shared.module";

// components
import { ProfileComponent } from "./profile.component";
import { ProfileRoutingModule } from "./profile.routing";

@NgModule({
  declarations: [ProfileComponent],
  imports: [SharedModule, ProfileRoutingModule],
  exports: [],
  providers: [],
})
export class ProfileModule {}
