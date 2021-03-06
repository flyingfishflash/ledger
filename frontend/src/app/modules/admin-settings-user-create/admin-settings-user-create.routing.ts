// angular
import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

// components
import { AdminSettingsUserCreateComponent } from "./admin-settings-user-create.component";

export const routes: Routes = [
  {
    path: "",
    component: AdminSettingsUserCreateComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminSettingsUserCreateRoutingModule {}
