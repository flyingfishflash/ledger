// angular
import { NgModule } from "@angular/core";

// components
import { HomeComponent } from "./home.component";

// core and shared
import { SharedModule } from "@shared/shared.module";

import { HomeRoutingModule } from "./home.routing";

@NgModule({
  declarations: [HomeComponent],
  imports: [SharedModule, HomeRoutingModule],
  exports: [],
  providers: [],
})
export class HomeModule {}
