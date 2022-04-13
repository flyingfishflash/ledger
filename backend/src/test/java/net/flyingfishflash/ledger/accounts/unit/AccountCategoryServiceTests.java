package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountCategoryService;

class AccountCategoryServiceTests {

  AccountCategoryService accountCategoryService = new AccountCategoryService();

  @Test
  void findAllAccountCategories_returnsAllCategoriesExceptRoot() {
    assertThat(accountCategoryService.findAllAccountCategories())
        .contains(
            Arrays.stream(AccountCategory.values())
                .filter(e -> !e.equals(AccountCategory.ROOT))
                .toArray(AccountCategory[]::new));
  }

  @Test
  void findAccountCategoryByType_whenTypeIsRoot_returnRootCategory() {
    assertThat(accountCategoryService.findAccountCategoryByType(AccountType.ROOT.toString()))
        .isEqualTo(AccountCategory.ROOT);
  }

  @ParameterizedTest
  @EnumSource(
      value = AccountType.class,
      names = {"ASSET", "BANK", "CASH", "MUTUAL", "STOCK"})
  void findAccountCategoryByType_allTypesOfCategoryAsset(AccountType accountType) {
    assertThat(accountCategoryService.findAccountCategoryByType(accountType.toString()))
        .isEqualTo(AccountCategory.ASSET);
  }

  @ParameterizedTest
  @EnumSource(
      value = AccountType.class,
      names = {"CREDIT", "LIABILITY"})
  void findAccountCategoryByType_allTypesOfCategoryLiability(AccountType accountType) {
    assertThat(accountCategoryService.findAccountCategoryByType(accountType.toString()))
        .isEqualTo(AccountCategory.LIABILITY);
  }

  @Test
  void findAccountCategoryByType_whenTypeIsIncome_returnIncomeCategory() {
    assertThat(accountCategoryService.findAccountCategoryByType(AccountType.INCOME.toString()))
        .isEqualTo(AccountCategory.INCOME);
  }

  @Test
  void findAccountCategoryByType_whenTypeIsExpense_returnExpenseCategory() {
    assertThat(accountCategoryService.findAccountCategoryByType(AccountType.EXPENSE.toString()))
        .isEqualTo(AccountCategory.EXPENSE);
  }

  @Test
  void findAccountCategoryByType_whenTypeIsEquity_returnEquityCategory() {
    assertThat(accountCategoryService.findAccountCategoryByType(AccountType.EQUITY.toString()))
        .isEqualTo(AccountCategory.EQUITY);
  }
}
