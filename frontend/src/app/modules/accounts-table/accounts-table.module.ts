// modules (angular)
import { NgModule } from "@angular/core";

// components
import { AccountsTableComponent } from "./accounts-table.component";
import { AccountsTableRoutingModule } from "./accounts-table.routing";

// core and shared
import { SharedModule } from "@shared/shared.module";

@NgModule({
  declarations: [AccountsTableComponent],
  imports: [SharedModule, AccountsTableRoutingModule],
  exports: [],
  providers: [],
})
export class AccountsTableModule {}
