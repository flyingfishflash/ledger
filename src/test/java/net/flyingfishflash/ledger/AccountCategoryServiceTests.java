package net.flyingfishflash.ledger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;
import net.flyingfishflash.ledger.accounts.AccountCategory;
import net.flyingfishflash.ledger.accounts.AccountCategoryService;
import net.flyingfishflash.ledger.accounts.AccountType;
import org.junit.jupiter.api.Test;

public class AccountCategoryServiceTests {

  AccountCategoryService accountCategoryService = new AccountCategoryService();

  @Test
  public void testFindAllAccountCategoriesSize() {
    List<AccountCategory> accountCategoryList = accountCategoryService.findAllAccountCategories();
    assertEquals(5, accountCategoryList.size());
  }

  @Test
  public void testFindAllAccountCategoriesContents() {
    List<AccountCategory> accountCategoryList = accountCategoryService.findAllAccountCategories();
    accountCategoryList.sort(Comparator.comparing(AccountCategory::name));
    assertEquals(AccountCategory.Asset, accountCategoryList.get(0));
    assertEquals(AccountCategory.Equity, accountCategoryList.get(1));
    assertEquals(AccountCategory.Expense, accountCategoryList.get(2));
    assertEquals(AccountCategory.Income, accountCategoryList.get(3));
    assertEquals(AccountCategory.Liability, accountCategoryList.get(4));
  }

  // Account Category should be Root
  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsRoot() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Root.toString());
    assertEquals((AccountCategory.Root), c);
  }

  // Account Category should be Asset
  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsAsset() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Asset.toString());
    assertEquals((AccountCategory.Asset), c);
  }

  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsBank() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Bank.toString());
    assertEquals((AccountCategory.Asset), c);
  }

  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsCash() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Cash.toString());
    assertEquals((AccountCategory.Asset), c);
  }

  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsMutual() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Mutual.toString());
    assertEquals((AccountCategory.Asset), c);
  }

  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsStock() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Stock.toString());
    assertEquals((AccountCategory.Asset), c);
  }

  // Account Category should be Liability
  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsCredit() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Credit.toString());
    assertEquals((AccountCategory.Liability), c);
  }

  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsLiability() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Liability.toString());
    assertEquals((AccountCategory.Liability), c);
  }

  // Account Category should be Income
  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsIncome() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Income.toString());
    assertEquals((AccountCategory.Income), c);
  }

  // Account Category should be Expense
  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsExpense() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Expense.toString());
    assertEquals((AccountCategory.Expense), c);
  }

  // Account Category should be Equity
  @Test
  public void testFindAccountCategoryByTypeWhenTypeIsEquity() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.Equity.toString());
    assertEquals((AccountCategory.Equity), c);
  }
}
