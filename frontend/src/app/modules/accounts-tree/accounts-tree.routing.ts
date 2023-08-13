// angular
import { NgModule } from '@angular/core'
import { Routes, RouterModule } from '@angular/router'

// components
import { AccountsTreeComponent } from './accounts-tree.component'

export const routes: Routes = [
  /*   {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  }, */
  {
    path: '',
    component: AccountsTreeComponent,
  },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountsTreeRoutingModule {}
