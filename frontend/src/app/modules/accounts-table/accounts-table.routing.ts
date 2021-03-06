// modules (angular)
import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

// components
import { AccountsTableComponent } from "./accounts-table.component";

export const routes: Routes = [
  {
    path: "",
    component: AccountsTableComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountsTableRoutingModule {}
