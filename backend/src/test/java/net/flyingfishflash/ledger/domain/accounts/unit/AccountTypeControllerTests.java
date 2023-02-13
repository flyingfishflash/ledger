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
import net.flyingfishflash.ledger.domain.accounts.service.AccountTypeService;
import net.flyingfishflash.ledger.domain.accounts.web.AccountTypeController;

@ExtendWith(MockitoExtension.class)
class AccountTypeControllerTests {

  @Mock AccountTypeService mockAccountTypeService;
  @InjectMocks AccountTypeController accountTypeController;

  AccountTypeService realAccountTypeService;

  private MockMvc mvc;
  private JacksonTester<List<AccountType>> jsonAccountTypes;

  @BeforeEach
  public void setup() {

    realAccountTypeService = new AccountTypeService();

    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc = MockMvcBuilders.standaloneSetup(accountTypeController).build();
  }

  @Test
  void getAccountTypes() throws Exception {
    given(mockAccountTypeService.findAllAccountTypes())
        .willReturn(realAccountTypeService.findAllAccountTypes());
    assertThat(
            mvc.perform(get("/account-types"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonAccountTypes.write(realAccountTypeService.findAllAccountTypes()).getJson());
  }

  @ParameterizedTest
  @EnumSource(AccountCategory.class)
  void getAccountTypesByCategory(AccountCategory accountCategory) throws Exception {
    given(mockAccountTypeService.findAccountTypesByCategory("LoremIpsum"))
        .willReturn(realAccountTypeService.findAccountTypesByCategory(accountCategory.name()));
    assertThat(
            mvc.perform(get("/account-types/by-category?category=LoremIpsum"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(
            jsonAccountTypes
                .write(
                    realAccountTypeService.findAccountTypesByCategory(accountCategory.toString()))
                .getJson());
  }
}
