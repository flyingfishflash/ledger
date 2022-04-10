package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountType;

class AccountTypeTests {

  @Test
  void testAccountTypeEnum() {
    assertEquals(11, AccountType.values().length);
    List<AccountType> accountTypeList = Arrays.asList(AccountType.values());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.ASSET, accountTypeList.get(0));
    assertEquals(AccountType.BANK, accountTypeList.get(1));
    assertEquals(AccountType.CASH, accountTypeList.get(2));
    assertEquals(AccountType.CREDIT, accountTypeList.get(3));
    assertEquals(AccountType.EQUITY, accountTypeList.get(4));
    assertEquals(AccountType.EXPENSE, accountTypeList.get(5));
    assertEquals(AccountType.INCOME, accountTypeList.get(6));
    assertEquals(AccountType.LIABILITY, accountTypeList.get(7));
    assertEquals(AccountType.MUTUAL, accountTypeList.get(8));
    assertEquals(AccountType.ROOT, accountTypeList.get(9));
    assertEquals(AccountType.STOCK, accountTypeList.get(10));
  }
}
