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

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsAsset() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.Asset.toString());
    assertEquals(5, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Asset, accountTypeList.get(0));
    assertEquals(AccountType.Bank, accountTypeList.get(1));
    assertEquals(AccountType.Cash, accountTypeList.get(2));
    assertEquals(AccountType.Mutual, accountTypeList.get(3));
    assertEquals(AccountType.Stock, accountTypeList.get(4));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsEquity() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.Equity.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Equity, accountTypeList.get(0));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsExpense() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.Expense.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Expense, accountTypeList.get(0));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsIncome() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.Income.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Income, accountTypeList.get(0));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsLiability() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.Liability.toString());
    assertEquals(2, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Credit, accountTypeList.get(0));
    assertEquals(AccountType.Liability, accountTypeList.get(1));
  }

  @Test
  public void testFindAccountTypesByCategoryWhenCategoryIsRoot() {
    List<AccountType> accountTypeList =
        accountTypeService.findAccountTypesByCategory(AccountCategory.Root.toString());
    assertEquals(1, accountTypeList.size());
    accountTypeList.sort(Comparator.comparing(AccountType::name));
    assertEquals(AccountType.Root, accountTypeList.get(0));
  }
}
