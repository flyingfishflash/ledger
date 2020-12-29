// angular
import { NgModule } from "@angular/core";

// components
import { ProfileComponent } from "./profile.component";
import { ProfileRoutingModule } from "./profile.routing";

// core and shared
import { SharedModule } from "@shared/shared.module";

@NgModule({
  declarations: [ProfileComponent],
  imports: [SharedModule, ProfileRoutingModule],
  exports: [],
  providers: [],
})
export class ProfileModule {}
