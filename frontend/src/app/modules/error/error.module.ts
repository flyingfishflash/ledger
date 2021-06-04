// angular
import { NgModule } from "@angular/core";

// components
import { ErrorComponent } from "./error.component";
import { ErrorRoutingModule } from "./error.routing";

// core and shared
import { SharedModule } from "@shared/shared.module";

@NgModule({
  declarations: [ErrorComponent],
  imports: [SharedModule, ErrorRoutingModule],
  exports: [],
  providers: [],
})
export class ErrorModule {}
