// angular
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { Routes } from "@angular/router";

// components
import { ProfileComponent } from "./profile.component";

export const routes: Routes = [
  /*   {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  }, */
  {
    path: "",
    component: ProfileComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProfileRoutingModule {}
