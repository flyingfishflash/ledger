// modules (angular)
import { NgModule } from "@angular/core";

// core and shared
import { SharedModule } from "@shared/shared.module";

import { AccountsTreeComponent } from "./accounts-tree.component";
import { AccountsTreeRoutingModule } from "./accounts-tree.routing";

@NgModule({
  declarations: [AccountsTreeComponent],
  imports: [SharedModule, AccountsTreeRoutingModule],
  exports: [],
  providers: [],
})
export class AccountsTreeModule {}
