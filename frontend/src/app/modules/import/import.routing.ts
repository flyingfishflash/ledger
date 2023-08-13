// angular
import { NgModule } from '@angular/core'
import { Routes, RouterModule } from '@angular/router'

// components
import { ImportComponent } from './import.component'

export const routes: Routes = [
  {
    path: '',
    component: ImportComponent,
  },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ImportRoutingModule {}
