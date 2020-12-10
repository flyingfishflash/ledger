// angular
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Routes } from "@angular/router";

// components
import { AdminSettingsComponent } from "./admin-settings.component";

export const routes: Routes = [
  {
    path: "",
    component: AdminSettingsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminSettingsRoutingModule {}
