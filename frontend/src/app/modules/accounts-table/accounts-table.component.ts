// angular
import { Component, OnInit } from '@angular/core'

// third party
import { throwError } from 'rxjs'

// core and shared
import { IAccount } from '../../shared/accounts/account'
import { AccountsService } from '../../shared/accounts/accounts.service'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('accounts-table.component')

@Component({
  selector: 'app-accounts-table',
  templateUrl: './accounts-table.component.html',
  styleUrls: ['./accounts-table.component.css'],
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
