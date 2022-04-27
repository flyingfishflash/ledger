package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.dto.AccountCreateRequest;
import net.flyingfishflash.ledger.accounts.data.dto.AccountRecord;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.accounts.web.AccountController;

@ExtendWith(MockitoExtension.class)
class AccountControllerTests {

  @Mock AccountService accountService;
  @InjectMocks AccountController accountController;

  private MockMvc mvc;

  private JacksonTester<Collection<Account>> jsonAccountCollection;
  private JacksonTester<AccountRecord> jsonAccountRecord;
  private JacksonTester<AccountCreateRequest> jsonCreateAccount;

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

  private AccountRecord prepareAccountRecord() {
    return new AccountRecord(
        null,
        null,
        null,
        null,
        null,
        null,
        false,
        1L,
        null,
        "Lorem ipsum",
        null,
        9999L,
        false,
        false,
        9999L,
        9999L,
        9999L);
  }

  @Test
  void getAccount() throws Exception {
    var accountRecord = prepareAccountRecord();
    given(accountService.findById(anyLong())).willReturn(new Account());
    given(accountService.mapEntityToRecord(any(Account.class))).willReturn(accountRecord);
    assertThat(
            mvc.perform(get("/api/v1/ledger/accounts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonAccountRecord.write(accountRecord).getJson());
  }

  @Test
  void getAccounts() throws Exception {
    var accountList = new ArrayList<Account>(1);
    var account1 = new Account();
    account1.setGuid("Any Guid");
    accountList.add(account1);
    given(accountService.findAllAccounts()).willReturn(accountList);
    assertThat(
            mvc.perform(get("/api/v1/ledger/accounts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonAccountCollection.write(accountList).getJson());
  }

  @Test
  void postAccounts() throws Exception {
    var accountRecord = prepareAccountRecord();
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "PREV_SIBLING",
            "Lorem ipsum dolor sit amet",
            null,
            2L,
            false,
            null,
            false);
    given(accountService.createAccount(any(AccountCreateRequest.class))).willReturn(new Account());
    given(accountService.mapEntityToRecord(any(Account.class))).willReturn(accountRecord);
    assertThat(
            mvc.perform(
                    post("/api/v1/ledger/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateAccount.write(accountCreateRequest).getJson()))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonAccountRecord.write(accountRecord).getJson());
  }

  @Test
  void postAccounts_whenRequestIsInvalid_returnHttp400() throws Exception {
    var accountCreateRequest =
        new AccountCreateRequest(
            "Lorem Ipsum",
            null,
            true,
            "FIRST_CHILD1",
            "Lorem Ipsum",
            "Lorem Ipsum",
            0L,
            null,
            0L,
            true);
    mvc.perform(
            post("/api/v1/ledger/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateAccount.write(accountCreateRequest).getJson()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteByAccountId() throws Exception {
    var requestParameter = "1";
    var longName = "Lorem Ipsum";
    var account1 = new Account();
    account1.setLongName(longName);
    given(accountService.findById(anyLong())).willReturn(account1);
    assertThat(
            mvc.perform(
                    delete("/api/v1/ledger/accounts/delete?accountId=" + requestParameter)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo("{\"message\":\"Deleted account: " + longName + "\"}");
  }

  @Test
  void postInsertAsNextSibling() throws Exception {
    var requestParameter = "1";
    mvc.perform(post("/api/v1/ledger/accounts/insert-as-next-sibling?id=" + requestParameter))
        .andExpect(status().isCreated());
  }

  @Test
  void postInsertAsPrevSibling() throws Exception {
    var requestParameter = "1";
    mvc.perform(post("/api/v1/ledger/accounts/insert-as-prev-sibling?id=" + requestParameter))
        .andExpect(status().isCreated());
  }

  @Test
  void getEligibleParentAccounts() throws Exception {
    var requestParameter = "1";
    var accountList = new ArrayList<Account>(1);
    var account1 = new Account();
    account1.setGuid("Any Guid");
    accountList.add(account1);
    given(accountService.findById(anyLong())).willReturn(new Account());
    given(accountService.getEligibleParentAccounts(any(Account.class))).willReturn(accountList);
    assertThat(
            mvc.perform(
                    get("/api/v1/ledger/accounts/" + requestParameter + "/eligible-parent-accounts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonAccountCollection.write(accountList).getJson());
  }
}
