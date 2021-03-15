// angular
import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

// components
import { LoginComponent } from "./login.component";

export const routes: Routes = [
  /*   {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  }, */
  {
    path: "",
    component: LoginComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LoginRoutingModule {}
