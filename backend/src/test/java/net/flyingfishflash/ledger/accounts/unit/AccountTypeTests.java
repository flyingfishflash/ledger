package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountType;

class AccountTypeTests {

  @Test
  void accountTypeEnum_contentIsElevenSpecificAccountTypes() {
    assertThat(AccountType.values())
        .containsExactly(
            AccountType.ROOT,
            AccountType.ASSET,
            AccountType.BANK,
            AccountType.CASH,
            AccountType.CREDIT,
            AccountType.EQUITY,
            AccountType.EXPENSE,
            AccountType.INCOME,
            AccountType.LIABILITY,
            AccountType.MUTUAL,
            AccountType.STOCK);
  }
}
