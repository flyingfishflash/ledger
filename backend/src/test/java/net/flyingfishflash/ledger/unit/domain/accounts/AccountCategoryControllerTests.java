package net.flyingfishflash.ledger.unit.domain.accounts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.accounts.service.AccountCategoryService;
import net.flyingfishflash.ledger.domain.accounts.web.AccountCategoryController;

/**
 * Unit tests for {@link AccountCategoryController}
 *
 * <p>Testing the various method response objects directly without filtering through controller
 * advice or serialization
 */
@ExtendWith(MockitoExtension.class)
class AccountCategoryControllerTests {

  @Mock AccountCategoryService mockAccountCategoryService;
  @InjectMocks AccountCategoryController accountCategoryController;

  AccountCategoryService realAccountCategoryService;

  private static final MockHttpServletRequest mockRequest =
      new MockHttpServletRequest("Lorem Ipsum", "lorem/ipsum");

  @BeforeEach
  public void setup() {
    realAccountCategoryService = new AccountCategoryService();
  }

  @Test
  void getAccountCategories() {
    var content = realAccountCategoryService.findAllAccountCategories();
    given(mockAccountCategoryService.findAllAccountCategories()).willReturn(content);

    assertThat(accountCategoryController.findAllAccountCategories(mockRequest))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(content, "Lorem Ipsum", "Lorem Ipsum", URI.create("lorem/ipsum")));
  }

  @ParameterizedTest(name = "{0}")
  @EnumSource(AccountType.class)
  void getAccountCategoriesByType(AccountType accountType) {
    var content = realAccountCategoryService.findAccountCategoryByType(accountType.toString());
    given(mockAccountCategoryService.findAccountCategoryByType(accountType.toString()))
        .willReturn(content);

    assertThat(
            accountCategoryController.findAccountCategoriesByType(
                mockRequest, accountType.toString()))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(content, "Lorem Ipsum", "Lorem Ipsum", URI.create("lorem/ipsum")));
  }
}
