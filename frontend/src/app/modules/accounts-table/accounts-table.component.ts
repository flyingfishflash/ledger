// angular
import { Component, OnInit } from '@angular/core'

// third party
import { throwError } from 'rxjs'

// core and shared
import { NgFor, NgIf } from '@angular/common'
import { FormsModule } from '@angular/forms'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatOptionModule } from '@angular/material/core'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatSelectModule } from '@angular/material/select'
import { MatTableModule } from '@angular/material/table'
import { Logger } from '../../core/logging/logger.service'
import { IAccount } from '../../shared/accounts/account'
import { AccountsService } from '../../shared/accounts/accounts.service'
import { PadWithSpacesPipe } from '../../shared/pipes/pad-with-spaces.pipe'

const log = new Logger('accounts-table.component')

@Component({
  selector: 'app-accounts-table',
  templateUrl: './accounts-table.component.html',
  styleUrls: ['./accounts-table.component.css'],
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    NgFor,
    MatInputModule,
    FormsModule,
    NgIf,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    PadWithSpacesPipe,
  ],
})
export class AccountsTableComponent implements OnInit {
  _listFilter: string = ''
  accounts: IAccount[] = []
  accountCategories: string[] = []
  componentHeading = 'Accounts Table'
  displayedColumns: string[] = ['name', 'code', 'description', 'action']
  errorMessage: any
  filteredAccounts: IAccount[]

  constructor(private accountsService: AccountsService) {
    this.filteredAccounts = this.accounts
  }

  get listFilter(): string {
    return this._listFilter
  }

  set listFilter(value: string) {
    this._listFilter = value
    this.filteredAccounts = this._listFilter
      ? this.performFilter(this._listFilter)
      : this.accounts
  }

  performFilter(filterBy: string): IAccount[] {
    filterBy = filterBy.toLocaleLowerCase()
    return this.accounts.filter((account: IAccount) =>
      account.name.toLocaleLowerCase().includes(filterBy),
    )
  }

  ngOnInit(): void {
    this.accountsService.getAccounts().subscribe({
      next: (res) => {
        this.accounts = res
        this.filteredAccounts = this.accounts
        // },
        // error: (err) => {
        //   log.debug(`zzz: ${err}`);
        //   this.handleError(err);
      },
    })

    this.accountsService.getAccountCategories().subscribe({
      next: (res) => {
        this.accountCategories = res
      },
      error: (err) => {
        this.handleError(err)
      },
    })
  }

  handleError(error: any) {
    log.debug(error)
    let errorMessage = ''
    if (error.error instanceof ErrorEvent) {
      errorMessage = `A client internal error occurred:\nError Message: ${error.error.message}`
    } else {
      errorMessage = `A server-side error occured:\nError Status: ${error.status}\nError Message: ${error.message}`
    }
    this.errorMessage = errorMessage
    log.error(errorMessage)
    return throwError(() => error)
  }
}
