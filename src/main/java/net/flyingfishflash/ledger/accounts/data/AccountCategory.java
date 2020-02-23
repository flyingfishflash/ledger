package net.flyingfishflash.ledger.accounts.data;

public enum AccountCategory {
  Root(null),
  Asset(NormalBalance.DEBIT),
  Liability,
  Equity,
  Income,
  Expense(NormalBalance.DEBIT);

  private NormalBalance normalBalance = NormalBalance.CREDIT;

  AccountCategory() {}

  AccountCategory(NormalBalance normalBalance) {
    this.normalBalance = normalBalance;
  }

  NormalBalance getNormalBalance() {
    return this.normalBalance;
  }
}
