package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;

class AccountCategoryTests {

  @Test
  void accountCategoryEnumValues_verifyEnumContainsExactly() {
    assertThat(AccountCategory.values())
        .containsExactly(
            AccountCategory.ROOT,
            AccountCategory.ASSET,
            AccountCategory.LIABILITY,
            AccountCategory.EQUITY,
            AccountCategory.INCOME,
            AccountCategory.EXPENSE);
  }
}
