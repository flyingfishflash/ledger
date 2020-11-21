import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

import { AccountsTreeComponent } from "./accounts-tree/accounts-tree.component";
import { AccountsTableComponent } from "./accounts-table/accounts-table.component";
import { AdminSettingsComponent } from "./admin-settings/admin-settings.component";
import { AdminSettingsUserCreateComponent } from "./admin-settings-user-create/admin-settings-user-create.component";
import { ImportComponent } from "./import/import.component";
import { HomeComponent } from "./home/home.component";
import { LoginComponent } from "./login/login.component";
import { ProfileComponent } from "./profile/profile.component";
import { AuthGuard } from "./_helpers/auth.guard";
import { Role } from "./_models/role";

const routes: Routes = [
  { path: "", component: LoginComponent, canActivate: [AuthGuard] },
  { path: "profile", component: ProfileComponent },
  {
    path: "admin/settings",
    component: AdminSettingsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: "admin/settings/user/create",
    component: AdminSettingsUserCreateComponent,
    canActivate: [AuthGuard],
  },
  { path: "accounts-tree", component: AccountsTreeComponent },
  { path: "accounts-table", component: AccountsTableComponent },
  { path: "home", component: HomeComponent },
  { path: "import", component: ImportComponent },
  { path: "login", component: LoginComponent },
  { path: "**", redirectTo: "" },
  // { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      { enableTracing: false } // <-- debugging purposes only
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
