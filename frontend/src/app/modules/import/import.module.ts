// angular
import { NgModule } from "@angular/core";

// core and shared
import { SharedModule } from "@shared/shared.module";

// components
import { ImportComponent } from "./import.component";
import { ImportRoutingModule } from "./import.routing";

@NgModule({
  declarations: [ImportComponent],
  imports: [SharedModule, ImportRoutingModule],
  exports: [],
  providers: [],
})
export class ImportModule {}
