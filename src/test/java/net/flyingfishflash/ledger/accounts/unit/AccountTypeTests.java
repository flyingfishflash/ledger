package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import org.junit.jupiter.api.Test;

public class AccountTypeTests {

  @Test
  public void testAccountTypeEnum() {
    assertEquals(11, AccountType.values().length);
    List<AccountType> accountTypeList = Arrays.asList(AccountType.values());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Asset, accountTypeList.get(0));
    assertEquals(AccountType.Bank, accountTypeList.get(1));
    assertEquals(AccountType.Cash, accountTypeList.get(2));
    assertEquals(AccountType.Credit, accountTypeList.get(3));
    assertEquals(AccountType.Equity, accountTypeList.get(4));
    assertEquals(AccountType.Expense, accountTypeList.get(5));
    assertEquals(AccountType.Income, accountTypeList.get(6));
    assertEquals(AccountType.Liability, accountTypeList.get(7));
    assertEquals(AccountType.Mutual, accountTypeList.get(8));
    assertEquals(AccountType.Root, accountTypeList.get(9));
    assertEquals(AccountType.Stock, accountTypeList.get(10));
  }
}
