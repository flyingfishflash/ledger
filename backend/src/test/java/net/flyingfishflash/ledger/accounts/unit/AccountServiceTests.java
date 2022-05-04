package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.data.dto.AccountCreateRequest;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests {

  @Mock private AccountRepository mockAccountRepository;
  @InjectMocks private AccountService accountService;

  @Test
  void findAccountById() {
    long accountId = 99;
    given(mockAccountRepository.findById(accountId)).willReturn(Optional.of(new Account()));
    accountService.findById(accountId);
    verify(mockAccountRepository, times(1)).findById(accountId);
  }

  @Test
  void findAccountById_whenAccountNotFound_thenAccountNotFoundException() {
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(() -> accountService.findById(1L));
  }

  @Test
  void findAccountByGuid() {
    String guid = "Lorem ipsum dolor sit amet";
    given(mockAccountRepository.findByGuid(guid)).willReturn(Optional.of(new Account()));
    accountService.findByGuid(guid);
    verify(mockAccountRepository, times(1)).findByGuid(guid);
  }

  @Test
  void findAccountByGuid_whenAccountNotFound_thenAccountNotFoundException() {
    String guid = "Lorem ipsum dolor sit amet";
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(() -> accountService.findByGuid(guid));
  }

  @Test
  void findAllAccounts() {
    // The collection returned by the service method has removed one account from the collection
    // returned by the repository: the Root account
    int allAccountsSize = (int) StreamSupport.stream(allAccounts().spliterator(), false).count();
    given(mockAccountRepository.findRoot()).willReturn(Optional.of(account1()));
    given(mockAccountRepository.getTreeAsList(any(Account.class))).willReturn(allAccounts());
    assertThat(accountService.findAllAccounts())
        .hasSize(allAccountsSize - 1)
        .extracting("treeLeft", "parentId")
        .doesNotContain(tuple(1L, null));
  }

  @Test
  void getBaseLevelParent_whenTargetIsAccountId7_thenBaseLevelParentIsAccountId2() {
    given(mockAccountRepository.getParents(any(Account.class))).willReturn(allAccounts());
    assertThat(accountService.getBaseLevelParent(account7()))
        .usingRecursiveComparison()
        .isEqualTo(account2());
  }

  @Test
  void getEligibleParentAccounts_whenAccountId7_thenEligibleParentAccountIds2And8() {
    // based on the mocked account structure expect this to return an Iterable with 2 items:
    // account id 2, and account id 8
    given(mockAccountRepository.getTreeAsList(any(Account.class))).willReturn(treeAsList());
    assertThat(accountService.getEligibleParentAccounts(account7()))
        .extracting("id")
        .containsOnly(2L, 8L);
  }

  @Test
  void getEligibleParentAccounts_throwsEligibleParentAccountNotFoundException() {
    var accountId2 = account2();
    given(mockAccountRepository.getTreeAsList(any(Account.class))).willReturn(treeAsList());
    assertThatExceptionOfType(EligibleParentAccountNotFoundException.class)
        .isThrownBy(() -> accountService.getEligibleParentAccounts(accountId2));
  }

  @Test
  void createAccount_whenParentAccountTypeIsRoot_thenChildAccountTypeIsAsset() {
    var parentAccount = account2();
    parentAccount.setType(AccountType.ROOT);
    given(mockAccountRepository.newAccount(anyString())).willReturn(new Account());
    assertThat(accountService.createAccount(parentAccount).getType()).isEqualTo(AccountType.ASSET);
  }

  @Test
  void createAccount_whenParentAccountTypeIsNotRoot_thenChildAccountTypeIsEqualToParent() {
    Account parentAccount = account2();
    parentAccount.setType(AccountType.LIABILITY);
    given(mockAccountRepository.newAccount(anyString())).willReturn(new Account());
    assertThat(accountService.createAccount(parentAccount).getType())
        .isEqualTo(AccountType.LIABILITY);
  }

  @Test
  void createAccount_insertAsPrevSibling() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "PREV_SIBLING",
            "Lorem ipsum dolor sit amet",
            null,
            2L,
            true,
            8L,
            false);
    given(mockAccountRepository.findById(2L)).willReturn(Optional.of(account2()));
    given(mockAccountRepository.findById(8L)).willReturn(Optional.of(account8()));
    given(mockAccountRepository.findByGuid(anyString())).willReturn(Optional.of(account8()));
    accountService.createAccount(accountCreateRequest);
    verify(mockAccountRepository).findById(2L);
    verify(mockAccountRepository).findById(8L);
    verify(mockAccountRepository).findByGuid(anyString());
    verify(mockAccountRepository).insertAsPrevSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  void createAccount_whenPreviousSiblingAccountNotFound_thenAccountCreateException() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "PREV_SIBLING",
            "Lorem ipsum dolor sit amet",
            null,
            2L,
            true,
            8L, // invalid value
            false);
    given(mockAccountRepository.findById(2L)).willReturn(Optional.of(account2()));
    // simulate failure to identify the previous sibling of the subject account
    given(mockAccountRepository.findById(8L)).willReturn(Optional.empty());
    assertThatExceptionOfType(AccountCreateException.class)
        .isThrownBy(() -> accountService.createAccount(accountCreateRequest));
  }

  @Test
  void createAccount_insertAsNextSibling() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "NEXT_SIBLING",
            "Lorem ipsum dolor sit amet",
            null,
            2L,
            true,
            7L, // invalid value
            false);
    given(mockAccountRepository.findById(2L)).willReturn(Optional.of(account2()));
    given(mockAccountRepository.findById(7L)).willReturn(Optional.of(account7()));
    given(mockAccountRepository.findByGuid(anyString())).willReturn(Optional.of(account8()));
    accountService.createAccount(accountCreateRequest);
    var inOrder = inOrder(mockAccountRepository);
    inOrder.verify(mockAccountRepository).findById(2L);
    inOrder.verify(mockAccountRepository).findById(7L);
    inOrder
        .verify(mockAccountRepository)
        .insertAsNextSiblingOf(any(Account.class), any(Account.class));
    inOrder.verify(mockAccountRepository).findByGuid(anyString());
  }

  @Test
  void createAccount_whenNextSiblingAccountNotFound_thenAccountCreateException() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "NEXT_SIBLING",
            "Lorem ipsum dolor sit amet",
            null,
            2L,
            true,
            9L,
            false);
    given(mockAccountRepository.findById(2L)).willReturn(Optional.of(account2()));
    // simulate failure to identify the next sibling of the subject account
    given(mockAccountRepository.findById(9L)).willReturn(Optional.empty());
    assertThatExceptionOfType(AccountCreateException.class)
        .isThrownBy(() -> accountService.createAccount(accountCreateRequest));
  }

  @Test
  void createAccount_insertAsFirstChild() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "FIRST_CHILD",
            "Lorem ipsum dolor sit amet",
            null,
            9999L,
            true,
            null,
            false);
    given(mockAccountRepository.findById(9999L)).willReturn(Optional.of(account2()));
    given(mockAccountRepository.findByGuid(anyString())).willReturn(Optional.of(account7()));
    accountService.createAccount(accountCreateRequest);
    var inOrder = inOrder(mockAccountRepository);
    inOrder.verify(mockAccountRepository).findById(9999L);
    inOrder
        .verify(mockAccountRepository)
        .insertAsFirstChildOf(any(Account.class), any(Account.class));
    inOrder.verify(mockAccountRepository).findByGuid(anyString());
  }

  @Test
  void createAccount_insertAsLastChild() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "LAST_CHILD",
            "Lorem ipsum dolor sit amet",
            null,
            2L,
            true,
            2L,
            false);
    given(mockAccountRepository.findById(2L)).willReturn(Optional.of(account2()));
    given(mockAccountRepository.findByGuid(anyString())).willReturn(Optional.of(account7()));
    accountService.createAccount(accountCreateRequest);
    var inOrder = inOrder(mockAccountRepository);
    inOrder.verify(mockAccountRepository).findById(2L);
    inOrder
        .verify(mockAccountRepository)
        .insertAsLastChildOf(any(Account.class), any(Account.class));
    inOrder.verify(mockAccountRepository).findByGuid(anyString());
  }

  @Test
  void createAccount_whenCreatedAccountCantBeFound_thenAccountCreateException() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "LAST_CHILD",
            "Lorem ipsum dolor sit amet",
            null,
            9999L,
            true,
            9999L,
            false);
    // expect an AccountCreateException to be thrown because:
    // 1) the created account is never persisted due to mock repository
    // 2) we are not mocking a response from mockAccountRepository.findByGuid()
    given(mockAccountRepository.findById(anyLong())).willReturn(Optional.of(account1()));
    assertThatExceptionOfType(AccountCreateException.class)
        .isThrownBy(() -> accountService.createAccount(accountCreateRequest))
        .withRootCauseExactlyInstanceOf(AccountNotFoundException.class);
  }

  @Test
  void createAccount_whenNestedNodeManipulatorIsInvalid_thenAccountCreateException() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "INVALID_NODE_MANIPULATOR",
            "Lorem ipsum dolor sit amet",
            null,
            9999L,
            true,
            null,
            false);
    //    accountCreateRequest.parentId = 9999L;
    given(mockAccountRepository.findById(anyLong())).willReturn(Optional.of(account1()));
    assertThatExceptionOfType(AccountCreateException.class)
        .isThrownBy(() -> accountService.createAccount(accountCreateRequest))
        .withMessage(
            "Failed to create account: '"
                + accountCreateRequest.name()
                + "'. A valid nest node manipulator mode was not specified.");
  }

  @Test
  void createAccount_whenParentAccountNotFound_thenAccountCreateException() {
    var accountCreateRequest =
        new AccountCreateRequest(
            null,
            null,
            false,
            "INVALID_NODE_MANIPULATOR",
            "Lorem ipsum dolor sit amet",
            null,
            9999L,
            true,
            null,
            false);
    // simulate failure to identify the parent account of the account to be created
    given(mockAccountRepository.findById(anyLong())).willThrow(AccountNotFoundException.class);
    assertThatExceptionOfType(AccountCreateException.class)
        .isThrownBy(() -> accountService.createAccount(accountCreateRequest))
        .withRootCauseExactlyInstanceOf(AccountNotFoundException.class)
        .withMessage("Failed to identify the parent account of an account to be created");
  }

  @Test
  void deleteAllAccounts_verifyMethodCalls_whenNoRoot() {
    accountService.deleteAllAccounts();
    verify(mockAccountRepository, times(1)).findRoot();
    verify(mockAccountRepository, times(0)).removeSubTree(any(Account.class));
    verifyNoMoreInteractions(mockAccountRepository);
  }

  @Test
  void deleteAllAccounts() {
    given(mockAccountRepository.findRoot()).willReturn(Optional.of(new Account()));
    accountService.deleteAllAccounts();
    verify(mockAccountRepository, times(1)).findRoot();
    verify(mockAccountRepository, times(1)).removeSubTree(any(Account.class));
    verifyNoMoreInteractions(mockAccountRepository);
  }

  @Test
  void removeSingleAccount() {
    accountService.removeSingle(new Account());
    verify(mockAccountRepository, times(1)).removeSingle(any(Account.class));
  }

  @Test
  void removeSubTree() {
    accountService.removeSubTree(new Account());
    verify(mockAccountRepository, times(1)).removeSubTree(any(Account.class));
  }

  @Test
  void findRoot_whenRootAccountNotFound_thenAccountNotFoundException() {
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(() -> accountService.findRoot());
    verify(mockAccountRepository, times(1)).findRoot();
  }

  @Test
  void findRoot_whenRepositoryReturnsAccount_thenServiceReturnsItUnaltered() {
    given(mockAccountRepository.findRoot()).willReturn(Optional.of(new Account()));
    assertThat(accountService.findRoot()).isNotNull();
    verify(mockAccountRepository, times(1)).findRoot();
  }

  @Test
  void getPrevSibling_whenSiblingAccountNotFound_thenPrevSiblingAccountNotFoundException() {
    var account = new Account();
    assertThatExceptionOfType(PrevSiblingAccountNotFoundException.class)
        .isThrownBy(() -> accountService.getPrevSibling(account));
    verify(mockAccountRepository, times(1)).getPrevSibling(any(Account.class));
  }

  @Test
  void getPrevSibling_whenRepositoryReturnsPrevSibling_thenServiceReturnItUnaltered() {
    var account = account2();
    given(mockAccountRepository.getPrevSibling(any(Account.class)))
        .willReturn(Optional.of(account));
    assertThat(accountService.getPrevSibling(new Account())).isEqualTo(account);
    verify(mockAccountRepository, times(1)).getPrevSibling(any(Account.class));
  }

  @Test
  void getNextSibling_whenSiblingAccountNotFound_thenNextSiblingNotFoundException() {
    var account = new Account();
    assertThatExceptionOfType(NextSiblingAccountNotFoundException.class)
        .isThrownBy(() -> accountService.getNextSibling(account));
    verify(mockAccountRepository, times(1)).getNextSibling(any(Account.class));
  }

  @Test
  void getNextSibling_whenRepositoryReturnsNextSibling_thenServiceReturnItUnaltered() {
    var account = account2();
    given(mockAccountRepository.getNextSibling(any(Account.class)))
        .willReturn(Optional.of(account));
    assertThat(accountService.getNextSibling(new Account())).isEqualTo(account);
    verify(mockAccountRepository, times(1)).getNextSibling(any(Account.class));
  }

  @Test
  void insertAsFirstRoot() {
    accountService.insertAsFirstRoot(new Account());
    verify(mockAccountRepository, times(1)).insertAsFirstRoot(any(Account.class));
  }

  @Test
  void insertAsLastRoot() {
    accountService.insertAsLastRoot(new Account());
    verify(mockAccountRepository, times(1)).insertAsLastRoot(any(Account.class));
  }

  @Test
  void insertAsFirstChildOf() {
    accountService.insertAsFirstChildOf(new Account(), new Account());
    verify(mockAccountRepository, times(1))
        .insertAsFirstChildOf(any(Account.class), any(Account.class));
  }

  @Test
  void insertAsLastChildOf() {
    accountService.insertAsLastChildOf(new Account(), new Account());
    verify(mockAccountRepository, times(1))
        .insertAsLastChildOf(any(Account.class), any(Account.class));
  }

  @Test
  void insertAsPrevSiblingOf() {
    accountService.insertAsPrevSiblingOf(new Account(), new Account());
    verify(mockAccountRepository, times(1))
        .insertAsPrevSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  void insertAsNextSiblingOf() {
    accountService.insertAsNextSiblingOf(new Account(), new Account());
    verify(mockAccountRepository, times(1))
        .insertAsNextSiblingOf(any(Account.class), any(Account.class));
  }

  //  @Test
  //  void mapEntityToRecord() {
  //    assertThat(
  //            new AccountRecord(
  //                null, null, null, null, "account", null, false, null, null, null, null, null,
  // false,
  //                false, null, null, null))
  //        .isEqualTo(accountService.mapEntityToRecord(new Account()));
  //  }

  private Iterable<Account> treeAsList() {
    List<Account> treeAsList = new ArrayList<>();
    treeAsList.add(account2());
    treeAsList.add(account7());
    treeAsList.add(account8());
    return treeAsList;
  }

  private Iterable<Account> allAccounts() {
    List<Account> allAccounts = new ArrayList<>();
    allAccounts.add(account1());
    allAccounts.add(account2());
    allAccounts.add(account3());
    allAccounts.add(account4());
    allAccounts.add(account5());
    allAccounts.add(account6());
    allAccounts.add(account7());
    allAccounts.add(account8());
    return allAccounts;
  }

  private Account account1() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("96333e3dc3c6492e830333366fd5aa05");
    account.setId(1L);
    account.setType(AccountType.ROOT);
    account.setCode("account_id_1");
    account.setHidden(false);
    account.setName("Root");
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(1L);
    account.setTreeLevel(0L);
    account.setTreeRight(16L);
    return account;
  }

  private Account account2() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("595023e2aca5410291b76ce3dc88c0fc");
    account.setId(2L);
    account.setType(AccountType.ASSET);
    account.setCode("account_id_2");
    account.setHidden(false);
    account.setName("Assets");
    account.setParentId(1L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(2L);
    account.setTreeLevel(1L);
    account.setTreeRight(7L);
    return account;
  }

  private Account account3() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("27a81f756013451682b5645c5164fca9");
    account.setId(3L);
    account.setType(AccountType.LIABILITY);
    account.setCode("account_id_3");
    account.setHidden(false);
    account.setName("Liabilities");
    account.setParentId(1L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(8L);
    account.setTreeLevel(1L);
    account.setTreeRight(9L);
    return account;
  }

  private Account account4() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("f7b53c40dab043b398faca7b5a397f84");
    account.setId(4L);
    account.setType(AccountType.INCOME);
    account.setCode("account_id_4");
    account.setHidden(false);
    account.setName("Income");
    account.setParentId(1L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(10L);
    account.setTreeLevel(1L);
    account.setTreeRight(11L);
    return account;
  }

  private Account account5() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("707004c44ba44b22b3a0868b747767bb");
    account.setId(5L);
    account.setType(AccountType.EXPENSE);
    account.setCode("account_id_5");
    account.setHidden(false);
    account.setName("Expense");
    account.setParentId(1L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(12L);
    account.setTreeLevel(1L);
    account.setTreeRight(13L);
    return account;
  }

  private Account account6() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("2a6bd9b7521a4458a77d757fb1734c39");
    account.setId(6L);
    account.setType(AccountType.EQUITY);
    account.setCode("account_id_6");
    account.setHidden(false);
    account.setName("Equity");
    account.setParentId(1L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(14L);
    account.setTreeLevel(1L);
    account.setTreeRight(15L);
    return account;
  }

  private Account account7() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("8a142619411849b59e09edde53f1757b");
    account.setId(7L);
    account.setType(AccountType.ASSET);
    account.setCode("account_id_7");
    account.setHidden(false);
    account.setName("Financial Assets");
    account.setParentId(2L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(3L);
    account.setTreeLevel(2L);
    account.setTreeRight(4L);
    return account;
  }

  private Account account8() {
    // account guid is set on instantiation and will be different for each assertion
    Account account = new Account("bed4273d24bf4824ba75b7e32c55f30e");
    account.setId(8L);
    account.setType(AccountType.ASSET);
    account.setCode("account_id_8");
    account.setHidden(false);
    account.setName("Fixed Assets");
    account.setParentId(2L);
    account.setPlaceholder(true);
    account.setTaxRelated(false);
    account.setTreeLeft(5L);
    account.setTreeLevel(2L);
    account.setTreeRight(6L);
    return account;
  }
}
