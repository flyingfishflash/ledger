// angular
import { NgModule } from '@angular/core'
import { Routes, RouterModule } from '@angular/router'

// components
import { HomeComponent } from './home.component'

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full',
  },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
