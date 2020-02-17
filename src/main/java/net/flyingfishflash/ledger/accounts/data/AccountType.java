package net.flyingfishflash.ledger.accounts.data;

public enum AccountType {
  Root(AccountCategory.Root),
  Asset(AccountCategory.Asset),
  Bank(AccountCategory.Asset),
  Cash(AccountCategory.Asset),
  Credit(AccountCategory.Liability),
  Equity(AccountCategory.Equity),
  Expense(AccountCategory.Expense),
  Income(AccountCategory.Income),
  Liability(AccountCategory.Liability),
  Mutual(AccountCategory.Asset),
  Stock(AccountCategory.Asset);

  private AccountCategory accountCategory;

  AccountType() {}

  AccountType(AccountCategory accountCategory) {
    this.accountCategory = accountCategory;
  }

  public AccountCategory getAccountCategory() {
    return accountCategory;
  }
}
