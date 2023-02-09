package net.flyingfishflash.ledger.domain.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.accounts.service.AccountCategoryService;
import net.flyingfishflash.ledger.domain.accounts.web.AccountCategoryController;

@ExtendWith(MockitoExtension.class)
class AccountCategoryControllerTests {

  @Mock AccountCategoryService mockAccountCategoryService;
  @InjectMocks AccountCategoryController accountCategoryController;

  AccountCategoryService realAccountCategoryService;

  private MockMvc mvc;
  private JacksonTester<List<AccountCategory>> jsonAccountCategories;
  private JacksonTester<AccountCategory> jsonAccountCategory;

  @BeforeEach
  public void setup() {

    realAccountCategoryService = new AccountCategoryService();

    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc = MockMvcBuilders.standaloneSetup(accountCategoryController).build();
  }

  @Test
  void getAccountCategories() throws Exception {
    given(mockAccountCategoryService.findAllAccountCategories())
        .willReturn(realAccountCategoryService.findAllAccountCategories());
    assertThat(
            mvc.perform(get("/api/v1/ledger/account-categories"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(
            jsonAccountCategories
                .write(realAccountCategoryService.findAllAccountCategories())
                .getJson());
  }

  @ParameterizedTest
  @EnumSource(AccountType.class)
  void getAccountCategoriesByType(AccountType accountType) throws Exception {
    given(mockAccountCategoryService.findAccountCategoryByType("LoremIpsum"))
        .willReturn(realAccountCategoryService.findAccountCategoryByType(accountType.name()));
    assertThat(
            mvc.perform(get("/api/v1/ledger/account-categories/by-type?type=LoremIpsum"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(
            jsonAccountCategory
                .write(realAccountCategoryService.findAccountCategoryByType(accountType.toString()))
                .getJson());
  }
}
