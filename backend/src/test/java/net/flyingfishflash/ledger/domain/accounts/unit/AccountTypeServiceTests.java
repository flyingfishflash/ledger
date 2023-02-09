package net.flyingfishflash.ledger.domain.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.accounts.service.AccountTypeService;

class AccountTypeServiceTests {

  AccountTypeService accountTypeService = new AccountTypeService();

  @Test
  void findAllAccountTypes_returnsAllAccountTypes() {
    assertThat(accountTypeService.findAllAccountTypes()).containsExactly(AccountType.values());
  }

  @Test
  void findAccountTypesByCategory_whenCategoryIsAsset() {
    assertThat(accountTypeService.findAccountTypesByCategory(AccountCategory.ASSET.toString()))
        .containsExactly(
            AccountType.ASSET,
            AccountType.BANK,
            AccountType.CASH,
            AccountType.MUTUAL,
            AccountType.STOCK);
  }

  @Test
  void findAccountTypesByCategory_whenCategoryIsLiability() {
    assertThat(accountTypeService.findAccountTypesByCategory(AccountCategory.LIABILITY.toString()))
        .containsExactly(AccountType.CREDIT, AccountType.LIABILITY);
  }

  @Test
  void findAccountTypesByCategory_whenCategoryIsEquity_thenAccountTypesListIsEquity() {
    assertThat(accountTypeService.findAccountTypesByCategory(AccountCategory.EQUITY.toString()))
        .containsExactly(AccountType.EQUITY);
  }

  @Test
  void findAccountTypesByCategory_whenCategoryIsExpense_thenAccountTypesListIsExpense() {
    assertThat(accountTypeService.findAccountTypesByCategory(AccountCategory.EXPENSE.toString()))
        .containsExactly(AccountType.EXPENSE);
  }

  @Test
  void findAccountTypesByCategory_whenCategoryIsIncome_thenAccountTypesListIsIncome() {
    assertThat(accountTypeService.findAccountTypesByCategory(AccountCategory.INCOME.toString()))
        .containsExactly(AccountType.INCOME);
  }

  @Test
  void findAccountTypesByCategory_whenCategoryIsRoot_thenAccountTypesListIncludesRoot() {
    assertThat(accountTypeService.findAccountTypesByCategory(AccountCategory.ROOT.toString()))
        .containsExactly(AccountType.ROOT);
  }
}
