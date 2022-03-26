package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountTypeService;

public class AccountTypeServiceTests {

  AccountTypeService accountTypeService = new AccountTypeService();

  @Test
  public void testFindAllAccountTypesSize() {
    assertEquals(11, accountTypeService.findAllAccountTypes().size());
  }

  @Test
  public void testFindAllAccountTypesContents() {
    List<AccountType> accountTypeList = accountTypeService.findAllAccountTypes();
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

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsAsset() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.ASSET.toString());
    assertEquals(5, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.ASSET, accountTypeList.get(0));
    assertEquals(AccountType.BANK, accountTypeList.get(1));
    assertEquals(AccountType.CASH, accountTypeList.get(2));
    assertEquals(AccountType.MUTUAL, accountTypeList.get(3));
    assertEquals(AccountType.STOCK, accountTypeList.get(4));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsEquity() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.EQUITY.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.EQUITY, accountTypeList.get(0));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsExpense() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.EXPENSE.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.EXPENSE, accountTypeList.get(0));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsIncome() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.INCOME.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.INCOME, accountTypeList.get(0));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsLiability() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.LIABILITY.toString());
    assertEquals(2, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.CREDIT, accountTypeList.get(0));
    assertEquals(AccountType.LIABILITY, accountTypeList.get(1));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsRoot() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.ROOT.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.ROOT, accountTypeList.get(0));
  }
}
