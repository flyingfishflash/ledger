package net.flyingfishflash.ledger.domain.accounts.data;

public enum AccountType {
  ROOT(AccountCategory.ROOT),
  ASSET(AccountCategory.ASSET),
  BANK(AccountCategory.ASSET),
  CASH(AccountCategory.ASSET),
  CREDIT(AccountCategory.LIABILITY),
  EQUITY(AccountCategory.EQUITY),
  EXPENSE(AccountCategory.EXPENSE),
  INCOME(AccountCategory.INCOME),
  LIABILITY(AccountCategory.LIABILITY),
  MUTUAL(AccountCategory.ASSET),
  STOCK(AccountCategory.ASSET);

  private final AccountCategory accountCategory;

  AccountType(AccountCategory accountCategory) {
    this.accountCategory = accountCategory;
  }

  public AccountCategory getAccountCategory() {
    return accountCategory;
  }
}
