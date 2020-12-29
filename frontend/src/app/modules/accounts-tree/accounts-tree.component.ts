// angular
import { NestedTreeControl } from "@angular/cdk/tree";
import { Component } from "@angular/core";
import { OnInit } from "@angular/core";
import { MatTreeNestedDataSource } from "@angular/material/tree";

// core and shared
import { IAccount } from "@shared/accounts/account";
import { AccountsService } from "@shared/accounts/accounts.service";

interface IAccountNode {
  id: number;
  guid: string;
  name: string;
  longName: string;
  code: string;
  description: string;
  note: string;
  placeholder: boolean;
  hidden: boolean;
  taxRelated: boolean;
  category: string;
  type: string;
  normalBalance: string;
  currency: string;
  // TODO: need to create a commodity class
  // commodity: customCommodityType;
  treeLeft: number;
  treeRight: number;
  treeLevel: number;
  parentId: number;
  discriminator: string;
  rootNode: boolean;
  children?: IAccountNode[];
}

@Component({
  selector: "app-accounts-tree",
  templateUrl: "./accounts-tree.component.html",
  styleUrls: ["./accounts-tree.component.css"],
})
export class AccountsTreeComponent implements OnInit {
  componentHeading = "Accounts Tree";
  treeControl = new NestedTreeControl<IAccountNode>((node) => node.children);
  dataSource = new MatTreeNestedDataSource<IAccountNode>();
  //  accounts: IAccount[];

  constructor(private accountsService: AccountsService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData() {
    const paramData = {};

    this.accountsService.getAccountsTree().subscribe((res) => {
      res = res || [];

      if (this.dataSource) {
        this.dataSource.data = res;
      }
    }); // subscribe
  }

  hasChild = (_: number, node: IAccountNode) =>
    !!node.children && node.children.length > 0;
}
