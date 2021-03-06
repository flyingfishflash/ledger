package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;

public class AccountCategoryTests {

  @Test
  public void testAccountCategoryEnum() {
    assertEquals(6, AccountCategory.values().length);
    List<AccountCategory> accountCategoryList = Arrays.asList(AccountCategory.values());
    accountCategoryList.sort(Comparator.comparing(AccountCategory::name));
    assertEquals(AccountCategory.Asset, accountCategoryList.get(0));
    assertEquals(AccountCategory.Equity, accountCategoryList.get(1));
    assertEquals(AccountCategory.Expense, accountCategoryList.get(2));
    assertEquals(AccountCategory.Income, accountCategoryList.get(3));
    assertEquals(AccountCategory.Liability, accountCategoryList.get(4));
    assertEquals(AccountCategory.Root, accountCategoryList.get(5));
  }
}
