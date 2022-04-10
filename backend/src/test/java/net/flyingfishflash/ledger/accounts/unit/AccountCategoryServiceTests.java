package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountCategoryService;

class AccountCategoryServiceTests {

  AccountCategoryService accountCategoryService = new AccountCategoryService();

  @Test
  void testFindAllAccountCategoriesSize() {
    List<AccountCategory> accountCategoryList = accountCategoryService.findAllAccountCategories();
    assertEquals(5, accountCategoryList.size());
  }

  @Test
  void testFindAllAccountCategoriesContents() {
    List<AccountCategory> accountCategoryList = accountCategoryService.findAllAccountCategories();
    accountCategoryList.sort(Comparator.comparing(AccountCategory::name));
    assertEquals(AccountCategory.ASSET, accountCategoryList.get(0));
    assertEquals(AccountCategory.EQUITY, accountCategoryList.get(1));
    assertEquals(AccountCategory.EXPENSE, accountCategoryList.get(2));
    assertEquals(AccountCategory.INCOME, accountCategoryList.get(3));
    assertEquals(AccountCategory.LIABILITY, accountCategoryList.get(4));
  }

  // Account Category should be Root
  @Test
  void testFindAccountCategoryByTypeWhenTypeIsRoot() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.ROOT.toString());
    assertEquals(AccountCategory.ROOT, c);
  }

  // Account Category should be Asset
  @Test
  void testFindAccountCategoryByTypeWhenTypeIsAsset() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.ASSET.toString());
    assertEquals(AccountCategory.ASSET, c);
  }

  @Test
  void testFindAccountCategoryByTypeWhenTypeIsBank() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.BANK.toString());
    assertEquals(AccountCategory.ASSET, c);
  }

  @Test
  void testFindAccountCategoryByTypeWhenTypeIsCash() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.CASH.toString());
    assertEquals(AccountCategory.ASSET, c);
  }

  @Test
  void testFindAccountCategoryByTypeWhenTypeIsMutual() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.MUTUAL.toString());
    assertEquals(AccountCategory.ASSET, c);
  }

  @Test
  void testFindAccountCategoryByTypeWhenTypeIsStock() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.STOCK.toString());
    assertEquals(AccountCategory.ASSET, c);
  }

  // Account Category should be Liability
  @Test
  void testFindAccountCategoryByTypeWhenTypeIsCredit() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.CREDIT.toString());
    assertEquals(AccountCategory.LIABILITY, c);
  }

  @Test
  void testFindAccountCategoryByTypeWhenTypeIsLiability() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.LIABILITY.toString());
    assertEquals(AccountCategory.LIABILITY, c);
  }

  // Account Category should be Income
  @Test
  void testFindAccountCategoryByTypeWhenTypeIsIncome() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.INCOME.toString());
    assertEquals(AccountCategory.INCOME, c);
  }

  // Account Category should be Expense
  @Test
  void testFindAccountCategoryByTypeWhenTypeIsExpense() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.EXPENSE.toString());
    assertEquals(AccountCategory.EXPENSE, c);
  }

  // Account Category should be Equity
  @Test
  void testFindAccountCategoryByTypeWhenTypeIsEquity() {
    AccountCategory c =
        accountCategoryService.findAccountCategoryByType(AccountType.EQUITY.toString());
    assertEquals(AccountCategory.EQUITY, c);
  }
}
