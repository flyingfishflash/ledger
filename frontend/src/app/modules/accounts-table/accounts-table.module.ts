// modules (angular)
import { NgModule } from '@angular/core'

// core and shared
import { SharedModule } from '../../shared/shared.module'

// components
import { AccountsTableComponent } from './accounts-table.component'
import { AccountsTableRoutingModule } from './accounts-table.routing'

@NgModule({
  imports: [SharedModule, AccountsTableRoutingModule, AccountsTableComponent],
  exports: [],
  providers: [],
})
export class AccountsTableModule {}
