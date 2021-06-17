package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class AccountServiceTests {

  @InjectMocks private AccountService accountService;

  @Mock private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

  @Test
  public void testFindAccountById() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.of(new Account()));
    accountService.findById(1L);
    verify(accountRepository).findById(anyLong());
  }

  @Test
  public void testFindAccountById_AccountNotFoundException() {
    Throwable exception =
        assertThrows(
            AccountNotFoundException.class,
            () -> {
              accountService.findById(1L);
            });
    verify(accountRepository).findById(anyLong());
    assertEquals("Account not found for id 1", exception.getLocalizedMessage());
  }

  @Test
  public void testFindAccountByGuid() {
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(new Account()));
    accountService.findByGuid("Any Guid");
    verify(accountRepository).findByGuid(anyString());
  }

  @Test
  public void testFindAccountByGuid_AccountNotFoundException() {
    Throwable exception =
        assertThrows(
            AccountNotFoundException.class,
            () -> {
              accountService.findByGuid("Any Guid");
            });
    verify(accountRepository).findByGuid(anyString());
    assertEquals("Account not found for guid 'Any Guid'", exception.getLocalizedMessage());
  }

  @Test
  public void testFindAllAccounts() {

    when(accountRepository.findRoot()).thenReturn(Optional.of(accountId1()));
    when(accountRepository.getTreeAsList(any(Account.class))).thenReturn(allAccounts());
    Iterable<Account> findAllAccounts = accountService.findAllAccounts();
    long findAllAccountsSize = StreamSupport.stream(findAllAccounts.spliterator(), false).count();
    long allAccountsSize = StreamSupport.stream(allAccounts().spliterator(), false).count();
    boolean containsRoot =
        StreamSupport.stream(findAllAccounts.spliterator(), false)
            .anyMatch(z -> z.getTreeLeft() == 1L && z.getParentId() == null);
    // expect the Iterable returned by the service has removed one account from Iterable returned
    // from the repository (the Root account)
    assertEquals(findAllAccountsSize, allAccountsSize - 1);
    assertFalse(containsRoot);
    verify(accountRepository).getTreeAsList(any(Account.class));
  }

  @Test
  public void testGetBaseLevelParent() {

    when(accountRepository.getParents(any(Account.class))).thenReturn(allAccounts());
    Account baseLevelParent = accountService.getBaseLevelParent(accountId7());
    verify(accountRepository).getParents(any(Account.class));
    assertNotNull(baseLevelParent);
    assertEquals(2L, baseLevelParent.getId().longValue());
    assertEquals(1L, baseLevelParent.getParentId().longValue());
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testGetEligibleParentAccounts() {

    when(accountRepository.getTreeAsList(any(Account.class))).thenReturn(treeAsList());
    Iterable<Account> eligbleParents = accountService.getEligibleParentAccounts(accountId7());
    verify(accountRepository).getTreeAsList(any(Account.class));
    // based on our mocked account structure
    // expect this to return an Iterable with 2 items: account id 2, and account id 8
    long elligibleParentsSize = StreamSupport.stream(eligbleParents.spliterator(), false).count();
    boolean containsId2 =
        StreamSupport.stream(eligbleParents.spliterator(), false).anyMatch(z -> z.getId() == 2L);
    boolean containsId8 =
        StreamSupport.stream(eligbleParents.spliterator(), false).anyMatch(z -> z.getId() == 8L);
    assertNotNull(eligbleParents);
    assertEquals(2, elligibleParentsSize);
    assertTrue(containsId2);
    assertTrue(containsId8);
    // System.out.println(mockingDetails(accountRepository).printInvocations());

  }

  @Test
  public void testGetEligibleParentAccounts_EligibleParentAccountNotFoundException() {

    when(accountRepository.getTreeAsList(any(Account.class))).thenReturn(treeAsList());
    assertThrows(
        EligibleParentAccountNotFoundException.class,
        () -> {
          Iterable<Account> eligbleParents = accountService.getEligibleParentAccounts(accountId2());
        });
    // System.out.println(mockingDetails(accountRepository).printInvocations());

  }

  @Test
  public void testCreateAccount_ParentAccountTypeIsRoot() {
    Account parentAccount = accountId2();
    parentAccount.setType(AccountType.ROOT);
    when(accountRepository.newAccount(anyString())).thenReturn(new Account());
    assertEquals(AccountType.ASSET, accountService.createAccount(parentAccount).getType());
  }

  @Test
  public void testCreateAccount_ParentAccountTypeIsNotRoot() {
    Account parentAccount = accountId2();
    parentAccount.setType(AccountType.LIABILITY);
    when(accountRepository.newAccount(anyString())).thenReturn(new Account());
    assertEquals(AccountType.LIABILITY, accountService.createAccount(parentAccount).getType());
  }

  @Test
  public void testCreateAccount_InsertAsPrevSibling() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "PREV_SIBLING";
    accountCreateRequest.name = "Financial Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 8L;
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findById(8L)).thenReturn(Optional.of(accountId8()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId8()));
    Account newAccount = accountService.createAccount(accountCreateRequest);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findById(8L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsPrevSiblingOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsPrevSibling_AccountCreateException() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "PREV_SIBLING";
    accountCreateRequest.name = "Financial Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 8L; // invalid value
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    // simulate failure to identify the previous sibling of the subject account
    when(accountRepository.findById(8L)).thenReturn(Optional.empty());
    assertThrows(
        AccountCreateException.class,
        () -> {
          Account newAccount = accountService.createAccount(accountCreateRequest);
        });

    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsNextSibling() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "NEXT_SIBLING";
    accountCreateRequest.name = "Fixed Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 7L;
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findById(7L)).thenReturn(Optional.of(accountId7()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId8()));
    Account newAccount = accountService.createAccount(accountCreateRequest);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findById(7L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsNextSiblingOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsNextSibling_AccountCreateException() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "NEXT_SIBLING";
    accountCreateRequest.name = "Financial Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 9L; // invalid value
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    // simulate failure to identify the next sibling of the subject account
    when(accountRepository.findById(9L)).thenReturn(Optional.empty());
    assertThrows(
        AccountCreateException.class,
        () -> {
          Account newAccount = accountService.createAccount(accountCreateRequest);
        });

    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsFirstChild() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "FIRST_CHILD";
    accountCreateRequest.name = "Financial Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 2L;
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId7()));
    Account newAccount = accountService.createAccount(accountCreateRequest);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsFirstChildOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsLastChild() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "LAST_CHILD";
    accountCreateRequest.name = "Financial Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 2L;
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId7()));
    Account newAccount = accountService.createAccount(accountCreateRequest);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsLastChildOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_AccountParentIsRoot() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "LAST_CHILD";
    accountCreateRequest.name = "Any Account Name";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 2L;
    accountCreateRequest.taxRelated = false;

    ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);

    when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountId1()));
    // expect an AccountCreateException to be thrown because the created account is never persisted
    assertThrows(
        AccountCreateException.class,
        () -> {
          accountService.createAccount(accountCreateRequest);
        });
    verify(accountRepository, times(1))
        .insertAsLastChildOf(accountArgumentCaptor.capture(), any(Account.class));
    assertEquals(AccountType.ASSET, accountArgumentCaptor.getValue().getType());
  }

  @Test
  public void testCreateAccount_InvalidNestedNodeManipulator() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.mode = "INVALID_NODE_MANIPULATOR";
    accountCreateRequest.name = "Any Account Name";
    accountCreateRequest.parentId = 2L;

    when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountId1()));
    Exception exception =
        assertThrows(
            AccountCreateException.class,
            () -> {
              accountService.createAccount(accountCreateRequest);
            });
    assertEquals(
        "Failed to create account: '"
            + accountCreateRequest.name
            + "'. A valid nest node manipulator mode was not specified.",
        exception.getLocalizedMessage());
  }

  @Test
  public void testCreateAccount_AccountNotFoundException() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.name = "Any Account Name";
    accountCreateRequest.parentId = 2L;

    // simulate failure to identify the parent account of the account to be created
    when(accountRepository.findById(anyLong())).thenThrow(AccountNotFoundException.class);
    Exception exception =
        assertThrows(
            AccountCreateException.class,
            () -> {
              accountService.createAccount(accountCreateRequest);
            });
    verify(accountRepository, times(1)).findById(anyLong());
    assertEquals(
        "Failed to identify the parent account of an account to be created",
        exception.getLocalizedMessage());
    assertEquals(
        "net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException",
        exception.getCause().toString());
  }

  @Test
  public void testCreateAccount_AccountCreateException() {

    AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
    accountCreateRequest.hidden = false;
    accountCreateRequest.mode = "LAST_CHILD";
    accountCreateRequest.name = "Financial Assets";
    accountCreateRequest.parentId = 2L;
    accountCreateRequest.placeholder = true;
    accountCreateRequest.siblingId = 2L;
    accountCreateRequest.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId8()));
    // simulate failure to confirm the newly created account has been persisted to the database
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.empty());
    assertThrows(
        AccountCreateException.class,
        () -> {
          Account newAccount = accountService.createAccount(accountCreateRequest);
        });
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testDeleteAllAccounts_NoRoot() {
    accountService.deleteAllAccounts();
    verify(accountRepository, times(1)).findRoot();
    verify(accountRepository, times(0)).removeSubTree(any(Account.class));
  }

  @Test
  public void testDeleteAllAccounts() {
    when(accountRepository.findRoot()).thenReturn(Optional.of(new Account()));
    accountService.deleteAllAccounts();
    verify(accountRepository, times(2)).findRoot();
    verify(accountRepository, times(1)).removeSubTree(any(Account.class));
  }

  @Test
  public void testRemoveSingleAccount() {
    accountService.removeSingle(new Account());
    verify(accountRepository, times(1)).removeSingle(any(Account.class));
  }

  @Test
  public void testRemoveSubTree() {
    accountService.removeSubTree(new Account());
    verify(accountRepository, times(1)).removeSubTree(any(Account.class));
  }

  @Test
  public void testFindRoot_NoRoot() {
    assertThrows(
        AccountNotFoundException.class,
        () -> {
          accountService.findRoot();
        });
    verify(accountRepository, times(1)).findRoot();
  }

  @Test
  public void testFindRoot() {
    when(accountRepository.findRoot()).thenReturn(Optional.of(new Account()));
    assertNotNull(accountService.findRoot());
    verify(accountRepository, times(1)).findRoot();
  }

  @Test
  public void testGetPrevSibling_NoPrevSibling() {
    assertThrows(
        PrevSiblingAccountNotFoundException.class,
        () -> {
          accountService.getPrevSibling(new Account());
        });
    verify(accountRepository, times(1)).getPrevSibling(any(Account.class));
  }

  @Test
  public void testFindPrevSibling() {
    when(accountRepository.getPrevSibling(any(Account.class)))
        .thenReturn(Optional.of(new Account()));
    assertNotNull(accountService.getPrevSibling(new Account()));
    verify(accountRepository, times(1)).getPrevSibling(any(Account.class));
  }

  @Test
  public void testGetNextSibling_NoNextSibling() {
    assertThrows(
        NextSiblingAccountNotFoundException.class,
        () -> {
          accountService.getNextSibling(new Account());
        });
    verify(accountRepository, times(1)).getNextSibling(any(Account.class));
  }

  @Test
  public void testNextPrevSibling() {
    when(accountRepository.getNextSibling(any(Account.class)))
        .thenReturn(Optional.of(new Account()));
    assertNotNull(accountService.getNextSibling(new Account()));
    verify(accountRepository, times(1)).getNextSibling(any(Account.class));
  }

  @Test
  public void testInsertAsFirstRoot() {
    accountService.insertAsFirstRoot(new Account());
    verify(accountRepository, times(1)).insertAsFirstRoot(any(Account.class));
  }

  @Test
  public void testInsertAsLastRoot() {
    accountService.insertAsLastRoot(new Account());
    verify(accountRepository, times(1)).insertAsLastRoot(any(Account.class));
  }

  @Test
  public void testInsertAsFirstChildOf() {
    accountService.insertAsFirstChildOf(new Account(), new Account());
    verify(accountRepository, times(1))
        .insertAsFirstChildOf(any(Account.class), any(Account.class));
  }

  @Test
  public void testInsertAsLastChildOf() {
    accountService.insertAsLastChildOf(new Account(), new Account());
    verify(accountRepository, times(1)).insertAsLastChildOf(any(Account.class), any(Account.class));
  }

  @Test
  public void testInsertAsPrevSiblingOf() {
    accountService.insertAsPrevSiblingOf(new Account(), new Account());
    verify(accountRepository, times(1))
        .insertAsPrevSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  public void testInsertAsNextSiblingOf() {
    accountService.insertAsNextSiblingOf(new Account(), new Account());
    verify(accountRepository, times(1))
        .insertAsNextSiblingOf(any(Account.class), any(Account.class));
  }

  private Iterable<Account> treeAsList() {

    List<Account> treeAsList = new ArrayList<>();

    treeAsList.add(accountId2());
    treeAsList.add(accountId7());
    treeAsList.add(accountId8());

    return treeAsList;
  }

  private Iterable<Account> allAccounts() {

    List<Account> allAccounts = new ArrayList<>();

    allAccounts.add(accountId1());
    allAccounts.add(accountId2());
    allAccounts.add(accountId3());
    allAccounts.add(accountId4());
    allAccounts.add(accountId5());
    allAccounts.add(accountId6());
    allAccounts.add(accountId7());
    allAccounts.add(accountId8());

    return allAccounts;
  }

  private Account accountId1() {

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

  private Account accountId2() {

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

  private Account accountId3() {

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

  private Account accountId4() {

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

  private Account accountId5() {

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

  private Account accountId6() {

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

  private Account accountId7() {

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

  private Account accountId8() {

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
