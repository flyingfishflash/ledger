// modules (angular)
import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";

// components
import { AuthLayoutComponent } from "./layout/auth-layout/auth-layout.component";
import { ContentLayoutComponent } from "./layout/content-layout/content-layout.component";
import { ErrorLayoutComponent } from "./layout/error-layout/error-layout.component";

import { AuthGuard } from "@core/guards/auth.guard";
import { BasicAuthUserRole } from "@core/authentication/basic-auth-user-role";

const routes: Routes = [
  {
    path: "",
    redirectTo: "/login",
    pathMatch: "full",
  },
  {
    path: "",
    component: ContentLayoutComponent,
    //canActivate: [AuthGuard],
    children: [
      {
        path: "accounts-table",
        loadChildren: () =>
          import("@modules/accounts-table/accounts-table.module").then(
            (m) => m.AccountsTableModule
          ),
      },
      {
        path: "accounts-tree",
        loadChildren: () =>
          import("@modules/accounts-tree/accounts-tree.module").then(
            (m) => m.AccountsTreeModule
          ),
      },
      {
        path: "admin/settings",
        loadChildren: () =>
          import("@modules/admin-settings/admin-settings.module").then(
            (m) => m.AdminSettingsModule
          ),
      },
      {
        path: "admin/settings/user/create",
        loadChildren: () =>
          import(
            "@modules/admin-settings-user-create/admin-settings-user-create.module"
          ).then((m) => m.AdminSettingsUserCreateModule),
      },
      {
        path: "home",
        loadChildren: () =>
          import("@modules/home/home.module").then((m) => m.HomeModule),
      },
      {
        path: "import",
        loadChildren: () =>
          import("@modules/import/import.module").then((m) => m.ImportModule),
      },
      {
        path: "profile",
        loadChildren: () =>
          import("@modules/profile/profile.module").then(
            (m) => m.ProfileModule
          ),
      },
    ],
  },
  {
    path: "error",
    component: ErrorLayoutComponent,
    loadChildren: () =>
      import("@modules/error/error.module").then((m) => m.ErrorModule),
  },
  {
    path: "login",
    component: AuthLayoutComponent,
    loadChildren: () =>
      import("@modules/login/login.module").then((m) => m.LoginModule),
  },
  { path: "**", redirectTo: "/login" },
  // { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      { enableTracing: false, relativeLinkResolution: "legacy" } // <-- debugging purposes only
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
