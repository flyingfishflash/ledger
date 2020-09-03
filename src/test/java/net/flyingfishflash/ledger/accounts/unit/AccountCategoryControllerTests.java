package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountCategoryService;
import net.flyingfishflash.ledger.accounts.web.AccountCategoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AccountCategoryControllerTests {

  private MockMvc mvc;

  @InjectMocks AccountCategoryController accountCategoryController;

  @Mock AccountCategoryService accountCategoryService;

  AccountCategoryService accountCategoryServiceReal;

  private JacksonTester<List<AccountCategory>> jsonAccountCategories;
  private JacksonTester<AccountCategory> jsonAccountCategory;

  @BeforeEach
  public void setup() {

    accountCategoryServiceReal = new AccountCategoryService();

    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc = MockMvcBuilders.standaloneSetup(accountCategoryController).build();
  }

  @Test
  public void testFindAllAccountCategories() throws Exception {

    given(accountCategoryService.findAllAccountCategories())
        .willReturn(accountCategoryServiceReal.findAllAccountCategories());

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/account-categories")).andReturn().getResponse();

    verify(accountCategoryService, times(1)).findAllAccountCategories();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(
            jsonAccountCategories
                .write(accountCategoryServiceReal.findAllAccountCategories())
                .getJson());
  }

  @Test
  public void testFindAccountCategoryByType() throws Exception {

    given(accountCategoryService.findAccountCategoryByType("Asset"))
        .willReturn(accountCategoryServiceReal.findAccountCategoryByType(AccountType.Asset.name()));

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/account-categories/by-type?type=Asset"))
            .andReturn()
            .getResponse();

    verify(accountCategoryService, times(1)).findAccountCategoryByType(anyString());
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(
            jsonAccountCategory
                .write(
                    accountCategoryServiceReal.findAccountCategoryByType(
                        AccountType.Asset.toString()))
                .getJson());
  }
}
