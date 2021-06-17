package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.dto.AccountCreateRequest;
import net.flyingfishflash.ledger.accounts.data.dto.AccountDto;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.accounts.web.AccountController;

@ExtendWith(MockitoExtension.class)
class AccountControllerTests {

  private MockMvc mvc;

  @InjectMocks AccountController accountController;

  @Mock AccountService accountService;

  private JacksonTester<Collection<Account>> jsonAccountCollection;
  private JacksonTester<Account> jsonAccount;
  private JacksonTester<AccountDto> jsonAccountDto;
  private JacksonTester<AccountCreateRequest> jsonCreateAccountDto;

  @BeforeEach
  public void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc =
        MockMvcBuilders.standaloneSetup(accountController)
            // doesn't respect @Order annotation
            // .setControllerAdvice(new AdviceForStandardExceptions())
            // .setControllerAdvice(new AdviceForAccountExceptions())
            // .setControllerAdvice(new AdviceForGeneralExceptions())
            // .addFilters(new AccountFilter())
            .build();
  }

  @Test
  void testFindAllAccounts() throws Exception {

    List<Account> accountList = new ArrayList<Account>();
    Account account1 = new Account();
    account1.setGuid("Any Guid");
    accountList.add(account1);

    given(accountService.findAllAccounts()).willReturn(accountList);

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/accounts").accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(accountService, times(1)).findAllAccounts();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonAccountCollection.write(accountList).getJson());
  }

  @Test
  void testFindAccountById() throws Exception {

    Account account1 = new Account();
    AccountDto accountDto1 = new AccountDto(account1);
    given(accountService.findById(anyLong())).willReturn(account1);

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/accounts/1").accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonAccountDto.write(accountDto1).getJson());
  }

  @Test
  void testCreateAccount_BadRequest() throws Exception {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.setCode("string");
    accountCreateRequest.setHidden(true);
    accountCreateRequest.setMode("FIRST_CHILD1"); // invalid value
    accountCreateRequest.setName("string");
    accountCreateRequest.setNote("string");
    accountCreateRequest.setParentId(0L);
    accountCreateRequest.setSiblingId(0L);
    accountCreateRequest.setTaxRelated(true);

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonCreateAccountDto.write(accountCreateRequest).getJson()))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void testCreateAccount() throws Exception {

    Account account1 = new Account();
    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.setName("Any Name");
    accountCreateRequest.setParentId(2L);
    accountCreateRequest.setHidden(false);
    accountCreateRequest.setPlaceholder(false);
    accountCreateRequest.setTaxRelated(false);
    accountCreateRequest.setMode("PREV_SIBLING");

    given(accountService.createAccount(any(AccountCreateRequest.class))).willReturn(account1);

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonCreateAccountDto.write(accountCreateRequest).getJson()))
            .andReturn()
            .getResponse();
  }

  @Test
  void testDeleteAccountAndDescendants() throws Exception {

    String requestParameter = "1";
    var longName = "Lorem Ipsum";

    Account account1 = new Account();
    account1.setLongName(longName);
    given(accountService.findById(anyLong())).willReturn(account1);

    MockHttpServletResponse response =
        mvc.perform(
                delete("/api/v1/ledger/accounts/delete?accountId=" + requestParameter)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    assertThat(response.getContentAsString())
        .isEqualTo("{\"message\":\"Deleted account: " + longName + "\"}");
  }

  @Test
  void testInsertAsNextSibling() throws Exception {

    String requestParameter = "1";

    MockHttpServletResponse response =
        mvc.perform(post("/api/v1/ledger/accounts/insert-as-next-sibling?id=" + requestParameter))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  void testInsertAsPrevSibling() throws Exception {

    String requestParameter = "1";

    MockHttpServletResponse response =
        mvc.perform(post("/api/v1/ledger/accounts/insert-as-prev-sibling?id=" + requestParameter))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  void testGetEligibleParentAccountsOf() throws Exception {

    String requestParameter = "1";

    List<Account> accountList = new ArrayList<Account>();
    Account account1 = new Account();
    account1.setGuid("Any Guid");
    accountList.add(account1);

    given(accountService.findById(anyLong())).willReturn(new Account());
    given(accountService.getEligibleParentAccounts(any(Account.class))).willReturn(accountList);

    MockHttpServletResponse response =
        mvc.perform(
                get("/api/v1/ledger/accounts/" + requestParameter + "/eligible-parent-accounts")
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(accountService, times(1)).getEligibleParentAccounts(any(Account.class));
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonAccountCollection.write(accountList).getJson());
  }
}
