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
import net.flyingfishflash.ledger.accounts.data.dto.AccountDto;
import net.flyingfishflash.ledger.accounts.data.dto.CreateAccountDto;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.accounts.web.AccountController;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTests {

  private MockMvc mvc;

  @InjectMocks AccountController accountController;

  @Mock AccountService accountService;

  private JacksonTester<Collection<Account>> jsonAccountCollection;
  private JacksonTester<Account> jsonAccount;
  private JacksonTester<AccountDto> jsonAccountDto;
  private JacksonTester<CreateAccountDto> jsonCreateAccountDto;

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
  public void testFindAllAccounts() throws Exception {

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
  public void testFindAccountById() throws Exception {

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
  public void testCreateAccount_BadRequest() throws Exception {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.setCode("string");
    createAccountDto.setHidden(true);
    createAccountDto.setMode("FIRST_CHILD1"); // invalid value
    createAccountDto.setName("string");
    createAccountDto.setNote("string");
    createAccountDto.setParentId(0L);
    createAccountDto.setSiblingId(0L);
    createAccountDto.setTaxRelated(true);

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonCreateAccountDto.write(createAccountDto).getJson()))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void testCreateAccount() throws Exception {

    Account account1 = new Account();
    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.setName("Any Name");
    createAccountDto.setParentId(2L);
    createAccountDto.setHidden(false);
    createAccountDto.setPlaceholder(false);
    createAccountDto.setTaxRelated(false);
    createAccountDto.setMode("PREV_SIBLING");

    given(accountService.createAccount(any(CreateAccountDto.class))).willReturn(account1);

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonCreateAccountDto.write(createAccountDto).getJson()))
            .andReturn()
            .getResponse();
  }

  @Test
  public void testDeleteAccountAndDescendants() throws Exception {

    String requestParameter = "1";

    MockHttpServletResponse response =
        mvc.perform(
                delete("/api/v1/ledger/accounts/delete?accountId=" + requestParameter)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  @Test
  public void testInsertAsNextSibling() throws Exception {

    String requestParameter = "1";

    MockHttpServletResponse response =
        mvc.perform(post("/api/v1/ledger/accounts/insert-as-next-sibling?id=" + requestParameter))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  public void testInsertAsPrevSibling() throws Exception {

    String requestParameter = "1";

    MockHttpServletResponse response =
        mvc.perform(post("/api/v1/ledger/accounts/insert-as-prev-sibling?id=" + requestParameter))
            .andReturn()
            .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  public void testGetEligibleParentAccountsOf() throws Exception {

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
