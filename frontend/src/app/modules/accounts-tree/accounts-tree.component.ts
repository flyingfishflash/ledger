// angular
import { NestedTreeControl } from '@angular/cdk/tree'
import { Component, OnInit } from '@angular/core'
import { MatTreeNestedDataSource } from '@angular/material/tree'

// third party
import { throwError } from 'rxjs'

// core and shared
import { IAccount } from '../../shared/accounts/account'
import { AccountsService } from '../../shared/accounts/accounts.service'
import { ErrorDialogService } from '../../shared/errors/error-dialog.service'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('accounts-tree.component')

interface IAccountNode {
  id: number
  guid: string
  name: string
  longName: string
  code: string
  description: string
  note: string
  placeholder: boolean
  hidden: boolean
  taxRelated: boolean
  category: string
  type: string
  normalBalance: string
  currency: string
  // TODO: need to create a commodity class
  // commodity: customCommodityType;
  treeLeft: number
  treeRight: number
  treeLevel: number
  parentId: number
  discriminator: string
  rootNode: boolean
  children?: IAccountNode[]
}

@Component({
  selector: 'app-accounts-tree',
  templateUrl: './accounts-tree.component.html',
  styleUrls: ['./accounts-tree.component.css'],
})
export class AccountsTreeComponent implements OnInit {
  componentHeading = 'Accounts Tree'
  treeControl = new NestedTreeControl<IAccountNode>((node) => node.children)
  dataSource = new MatTreeNestedDataSource<IAccountNode>()
  errorMessage: any
  //  accounts: IAccount[];

  constructor(
    private accountsService: AccountsService,
    private errorDialogService: ErrorDialogService,
  ) {}

  ngOnInit(): void {
    this.fetchData()
  }

  fetchData() {
    const paramData = {}

    this.accountsService.getAccountsTree().subscribe({
      next: (res) => {
        res = res || []

        if (this.dataSource) {
          this.dataSource.data = res
        }
      },
      error: (err) => this.handleError(err),
    }) // subscribe
  }

  hasChild = (_: number, node: IAccountNode) =>
    !!node.children && node.children.length > 0

  handleError(error: any) {
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
