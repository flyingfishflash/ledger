// angular
import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

// components
import { ErrorComponent } from "./error.component";

export const routes: Routes = [
  /*   {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  }, */
  {
    path: "",
    component: ErrorComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ErrorRoutingModule {}
