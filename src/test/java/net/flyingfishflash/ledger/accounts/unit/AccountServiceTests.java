package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.data.dto.CreateAccountDto;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AccountServiceTests {

  @Mock private AccountRepository accountRepository = Mockito.mock(AccountRepository.class);

  private AccountService accountService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    this.accountService = new AccountService(accountRepository);
  }

  @Test
  public void testFindAccountById() {

    when(accountRepository.findById(1L)).thenReturn(Optional.of(accountId1()));
    Account account = accountService.findById(1L);
    verify(accountRepository).findById(1L);
    assertEquals(accountId1().getId(), account.getId());
    assertEquals(accountId1().getAccountCategory(), account.getAccountCategory());
    assertEquals(accountId1().getAccountType(), account.getAccountType());
    assertEquals(accountId1().getCode(), account.getCode());
    assertNotNull(accountId1().getGuid(), account.getGuid());
    assertEquals(accountId1().getHidden(), account.getHidden());
    assertEquals(accountId1().getName(), account.getName());
    assertEquals(accountId1().getPlaceholder(), account.getPlaceholder());
    assertEquals(accountId1().getTaxRelated(), account.getTaxRelated());
    assertEquals(accountId1().getTreeLeft(), account.getTreeLeft());
    assertEquals(accountId1().getTreeLevel(), account.getTreeLevel());
    assertEquals(accountId1().getTreeRight(), account.getTreeRight());
    assertEquals(accountId1().getParentId(), account.getParentId());
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testFindAccountByGuid() {

    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId1()));
    Account account = accountService.findByGuid(anyString());
    verify(accountRepository).findByGuid(anyString());
    assertNotNull(accountId1().getGuid(), account.getGuid());
    assertEquals(accountId1().getId(), account.getId());
    assertEquals(accountId1().getAccountCategory(), account.getAccountCategory());
    assertEquals(accountId1().getAccountType(), account.getAccountType());
    assertEquals(accountId1().getCode(), account.getCode());
    assertEquals(accountId1().getHidden(), account.getHidden());
    assertEquals(accountId1().getName(), account.getName());
    assertEquals(accountId1().getPlaceholder(), account.getPlaceholder());
    assertEquals(accountId1().getTaxRelated(), account.getTaxRelated());
    assertEquals(accountId1().getTreeLeft(), account.getTreeLeft());
    assertEquals(accountId1().getTreeLevel(), account.getTreeLevel());
    assertEquals(accountId1().getTreeRight(), account.getTreeRight());
    assertEquals(accountId1().getParentId(), account.getParentId());
    // System.out.println(mockingDetails(accountRepository).printInvocations());
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
    assertTrue(findAllAccountsSize == allAccountsSize - 1);
    assertFalse(containsRoot);
    verify(accountRepository).getTreeAsList(any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
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
          Iterable<Account> eligbleParents =
              accountService.getEligibleParentAccounts(accountId2());
        });
    // System.out.println(mockingDetails(accountRepository).printInvocations());

  }

  @Test
  public void testCreateAccount_InsertAsPrevSibling() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "PREV_SIBLING";
    createAccountDto.name = "Financial Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 8L;
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findById(8L)).thenReturn(Optional.of(accountId8()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId8()));
    Account newAccount = accountService.createAccount(createAccountDto);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findById(8L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsPrevSiblingOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsPrevSibling_AccountCreateException() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "PREV_SIBLING";
    createAccountDto.name = "Financial Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 8L; // invalid value
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    // simulate failure to identify the previous sibling of the subject account
    when(accountRepository.findById(8L)).thenReturn(Optional.empty());
    assertThrows(
        AccountCreateException.class,
        () -> {
          Account newAccount = accountService.createAccount(createAccountDto);
        });

    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsNextSibling() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "NEXT_SIBLING";
    createAccountDto.name = "Fixed Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 7L;
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findById(7L)).thenReturn(Optional.of(accountId7()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId8()));
    Account newAccount = accountService.createAccount(createAccountDto);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findById(7L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsNextSiblingOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsNextSibling_AccountCreateException() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "NEXT_SIBLING";
    createAccountDto.name = "Financial Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 9L; // invalid value
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    // simulate failure to identify the next sibling of the subject account
    when(accountRepository.findById(9L)).thenReturn(Optional.empty());
    assertThrows(
        AccountCreateException.class,
        () -> {
          Account newAccount = accountService.createAccount(createAccountDto);
        });

    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsFirstChild() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "FIRST_CHILD";
    createAccountDto.name = "Financial Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 2L;
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId7()));
    Account newAccount = accountService.createAccount(createAccountDto);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsFirstChildOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_InsertAsLastChild() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "LAST_CHILD";
    createAccountDto.name = "Financial Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 2L;
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.of(accountId7()));
    Account newAccount = accountService.createAccount(createAccountDto);

    verify(accountRepository).findById(2L);
    verify(accountRepository).findByGuid(anyString());
    verify(accountRepository).insertAsLastChildOf(any(Account.class), any(Account.class));
    // System.out.println(mockingDetails(accountRepository).printInvocations());
  }

  @Test
  public void testCreateAccount_AccountCreateException() {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.hidden = false;
    createAccountDto.mode = "LAST_CHILD";
    createAccountDto.name = "Financial Assets";
    createAccountDto.parentId = 2L;
    createAccountDto.placeholder = true;
    createAccountDto.siblingId = 2L;
    createAccountDto.taxRelated = false;

    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId2()));
    when(accountRepository.findById(2L)).thenReturn(Optional.of(accountId8()));
    // simulate failure to confirm the newly created account has been persisted to the database
    when(accountRepository.findByGuid(anyString())).thenReturn(Optional.empty());
    assertThrows(
        AccountCreateException.class,
        () -> {
          Account newAccount = accountService.createAccount(createAccountDto);
        });

    // System.out.println(mockingDetails(accountRepository).printInvocations());
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
    Account account = new Account();
    account.setId(1L);
    account.setAccountCategory(AccountCategory.Root);
    account.setAccountType(AccountType.Root);
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
    Account account = new Account();
    account.setId(2L);
    account.setAccountCategory(AccountCategory.Asset);
    account.setAccountType(AccountType.Asset);
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
    Account account = new Account();
    account.setId(3L);
    account.setAccountCategory(AccountCategory.Liability);
    account.setAccountType(AccountType.Liability);
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
    Account account = new Account();
    account.setId(4L);
    account.setAccountCategory(AccountCategory.Income);
    account.setAccountType(AccountType.Income);
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
    Account account = new Account();
    account.setId(5L);
    account.setAccountCategory(AccountCategory.Expense);
    account.setAccountType(AccountType.Expense);
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
    Account account = new Account();
    account.setId(6L);
    account.setAccountCategory(AccountCategory.Equity);
    account.setAccountType(AccountType.Equity);
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
    Account account = new Account();
    account.setId(7L);
    account.setAccountCategory(AccountCategory.Asset);
    account.setAccountType(AccountType.Asset);
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
    Account account = new Account();
    account.setId(8L);
    account.setAccountCategory(AccountCategory.Asset);
    account.setAccountType(AccountType.Asset);
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
