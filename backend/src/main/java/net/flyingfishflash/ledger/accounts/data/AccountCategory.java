package net.flyingfishflash.ledger.accounts.data;

public enum AccountCategory {
  ROOT(null),
  ASSET(NormalBalance.DEBIT),
  LIABILITY,
  EQUITY,
  INCOME,
  EXPENSE(NormalBalance.DEBIT);

  private NormalBalance normalBalance = NormalBalance.CREDIT;

  AccountCategory() {}

  AccountCategory(NormalBalance normalBalance) {
    this.normalBalance = normalBalance;
  }

  NormalBalance getNormalBalance() {
    return this.normalBalance;
  }
}
