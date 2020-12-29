// angular
import { Component } from "@angular/core";
import { OnInit } from "@angular/core";

// core and shared
import { IAccount } from "@shared/accounts/account";
import { AccountsService } from "@shared/accounts/accounts.service";

@Component({
  selector: "app-accounts-table",
  templateUrl: "./accounts-table.component.html",
  styleUrls: ["./accounts-table.component.css"],
})
export class AccountsTableComponent implements OnInit {
  componentHeading = "Accounts Table";
  displayedColumns: string[] = ["name", "code", "description", "action"];

  _listFilter: string;
  errorMessage: any;

  filteredAccounts: IAccount[];
  accounts: IAccount[];
  accountCategories: string[];

  constructor(private accountsService: AccountsService) {
    this.filteredAccounts = this.accounts;
  }

  performFilter(filterBy: string): IAccount[] {
    filterBy = filterBy.toLocaleLowerCase();
    return this.accounts.filter(
      (account: IAccount) =>
        account.name.toLocaleLowerCase().indexOf(filterBy) !== -1
    );
  }

  ngOnInit(): void {
    this.accountsService.getAccounts().subscribe({
      next: (accounts) => {
        this.accounts = accounts;
        this.filteredAccounts = this.accounts;
      },
      error: (err) => (this.errorMessage = err),
    });

    this.accountsService.getAccountCategories().subscribe({
      next: (accountCategories) => {
        this.accountCategories = accountCategories;
      },
      error: (err) => (this.errorMessage = err),
    });
  }

  get listFilter(): string {
    return this._listFilter;
  }

  set listFilter(value: string) {
    this._listFilter = value;
    this.filteredAccounts = this._listFilter
      ? this.performFilter(this._listFilter)
      : this.accounts;
  }
}
