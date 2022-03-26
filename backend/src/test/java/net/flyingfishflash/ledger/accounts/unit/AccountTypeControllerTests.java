package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

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

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountTypeService;
import net.flyingfishflash.ledger.accounts.web.AccountTypeController;

@ExtendWith(MockitoExtension.class)
public class AccountTypeControllerTests {

  private MockMvc mvc;

  @InjectMocks AccountTypeController accountTypeController;

  @Mock AccountTypeService accountTypeService;

  AccountTypeService accountTypeServiceReal;

  private JacksonTester<List<AccountType>> jsonAccountTypes;
  private JacksonTester<AccountType> jsonAccountType;

  @BeforeEach
  public void setup() {

    accountTypeServiceReal = new AccountTypeService();

    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc = MockMvcBuilders.standaloneSetup(accountTypeController).build();
  }

  @Test
  public void testFindAllAccountTypes() throws Exception {

    given(accountTypeService.findAllAccountTypes())
        .willReturn(accountTypeServiceReal.findAllAccountTypes());

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/account-types")).andReturn().getResponse();

    verify(accountTypeService, times(1)).findAllAccountTypes();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonAccountTypes.write(accountTypeServiceReal.findAllAccountTypes()).getJson());
  }

  @Test
  public void testFindAccountTypesByCategory() throws Exception {

    given(accountTypeService.findAccountTypesByCategory("Asset"))
        .willReturn(
            accountTypeServiceReal.findAccountTypesByCategory(AccountCategory.ASSET.name()));

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/account-types/by-category?category=Asset"))
            .andReturn()
            .getResponse();

    verify(accountTypeService, times(1)).findAccountTypesByCategory(anyString());
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(
            jsonAccountTypes
                .write(
                    accountTypeServiceReal.findAccountTypesByCategory(
                        AccountCategory.ASSET.toString()))
                .getJson());
  }
}
