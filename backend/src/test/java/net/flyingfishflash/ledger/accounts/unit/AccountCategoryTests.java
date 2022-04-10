package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;

class AccountCategoryTests {

  @Test
  void testAccountCategoryEnum() {
    assertEquals(6, AccountCategory.values().length);
    List<AccountCategory> accountCategoryList = Arrays.asList(AccountCategory.values());
    accountCategoryList.sort(Comparator.comparing(AccountCategory::name));
    assertEquals(AccountCategory.ASSET, accountCategoryList.get(0));
    assertEquals(AccountCategory.EQUITY, accountCategoryList.get(1));
    assertEquals(AccountCategory.EXPENSE, accountCategoryList.get(2));
    assertEquals(AccountCategory.INCOME, accountCategoryList.get(3));
    assertEquals(AccountCategory.LIABILITY, accountCategoryList.get(4));
    assertEquals(AccountCategory.ROOT, accountCategoryList.get(5));
  }
}
