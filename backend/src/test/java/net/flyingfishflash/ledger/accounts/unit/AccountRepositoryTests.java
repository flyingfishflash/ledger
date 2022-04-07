package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import pl.exsio.nestedj.NestedNodeRepository;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.foundation.IdentifierFactory;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryTests {

  @Mock private NestedNodeRepository<Long, Account> mockNodeRepository;
  @Spy @InjectMocks private AccountRepository spyAccountRepository;
  @MockBean EntityManager entityManagerMock;
  @InjectMocks AccountRepository accountRepository;

  @BeforeEach
  public void setUp() {
    entityManagerMock = Mockito.mock(EntityManager.class);
    ReflectionTestUtils.setField(spyAccountRepository, "entityManager", entityManagerMock);
  }

  @Test
  void testGetTreeAsList() {
    accountRepository.getTreeAsList(new Account());
    verify(mockNodeRepository, times(1)).getTreeAsList(any(Account.class));
  }

  @Test
  void testGetPrevSibling() {
    accountRepository.getPrevSibling(new Account());
    verify(mockNodeRepository, times(1)).getPrevSibling(any(Account.class));
  }

  @Test
  void testGetNextSibling() {
    accountRepository.getNextSibling(new Account());
    verify(mockNodeRepository, times(1)).getNextSibling(any(Account.class));
  }

  @Test
  void testGetParents() {
    accountRepository.getParents(new Account());
    verify(mockNodeRepository, times(1)).getParents(any(Account.class));
  }

  @Test
  void testRemoveSingle() {
    accountRepository.removeSingle(new Account());
    verify(mockNodeRepository, times(1)).removeSingle(any(Account.class));
  }

  @Test
  void testRemoveSubTree() {
    accountRepository.removeSubTree(new Account());
    verify(mockNodeRepository, times(1)).removeSubtree(any(Account.class));
  }

  @Test
  void testInsertAsFirstRoot() {
    Long rootLevelNodeCount = 0L;
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Root Account First");
    doReturn(rootLevelNodeCount).when(spyAccountRepository).rootLevelNodeCount();
    spyAccountRepository.insertAsFirstRoot(newAccount);
    verify(mockNodeRepository, times(1)).insertAsFirstRoot(any(Account.class));
  }

  @Test
  void testInsertAsLastRoot() {
    Long rootLevelNodeCount = 0L;
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Root Account Last");
    doReturn(rootLevelNodeCount).when(spyAccountRepository).rootLevelNodeCount();
    spyAccountRepository.insertAsLastRoot(newAccount);
    verify(mockNodeRepository, times(1)).insertAsLastRoot(any(Account.class));
  }

  @Test
  void testInsertAsLastRoot_AccountCreateException() {
    Long rootLevelNodeCount = 1L;
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Root Account Last");
    doReturn(rootLevelNodeCount).when(spyAccountRepository).rootLevelNodeCount();
    Throwable exception =
        assertThrows(
            AccountCreateException.class,
            () -> {
              spyAccountRepository.insertAsLastRoot(newAccount);
            });
    verify(mockNodeRepository, times(0)).insertAsLastRoot(any(Account.class));
    assertEquals(
        "A new root level account can't be created. Only one root level account may be present. Current root level node count: "
            + rootLevelNodeCount,
        exception.getLocalizedMessage());
  }

  @Test
  void testInsertAsLastChildOf() {
    String longName = "Dummy Long Name - Last Child Of";
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Last Child Of");
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class));
    spyAccountRepository.insertAsLastChildOf(newAccount, newAccount);
    verify(mockNodeRepository, times(1))
        .insertAsLastChildOf(any(Account.class), any(Account.class));
  }

  @Test
  void testInsertAsFirstChildOf() {
    String longName = "Dummy Long Name - First Child Of";
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("First Child Of");
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class));
    spyAccountRepository.insertAsFirstChildOf(newAccount, newAccount);
    verify(mockNodeRepository, times(1))
        .insertAsFirstChildOf(any(Account.class), any(Account.class));
  }

  @Test
  void testInsertAsPrevSiblingOf() {
    String longName = "Dummy Long Name - Prev Sibling Of";
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Prev Sibling Of");
    newAccount.setParentId(99L);
    doReturn(Optional.of(newAccount)).when(spyAccountRepository).findById(anyLong());
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class));
    spyAccountRepository.insertAsPrevSiblingOf(newAccount, newAccount);
    verify(mockNodeRepository, times(1))
        .insertAsPrevSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  void testInsertAsPrevSiblingOf_AccountNotFoundException() {
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Prev Sibling Of");
    newAccount.setParentId(99L);
    doReturn(Optional.empty()).when(spyAccountRepository).findById(anyLong());
    assertThrows(
        AccountNotFoundException.class,
        () -> {
          spyAccountRepository.insertAsPrevSiblingOf(newAccount, newAccount);
        });
    verify(mockNodeRepository, times(0))
        .insertAsPrevSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  void testInsertAsNextSiblingOf() {
    String longName = "Dummy Long Name - Next Sibling Of";
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Next Sibling Of");
    newAccount.setParentId(99L);
    doReturn(Optional.of(newAccount)).when(spyAccountRepository).findById(anyLong());
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class));
    spyAccountRepository.insertAsNextSiblingOf(newAccount, newAccount);
    verify(mockNodeRepository, times(1))
        .insertAsNextSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  void testInsertAsNextSiblingOf_AccountNotFoundException() {
    String guid = IdentifierFactory.getInstance().generateIdentifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Next Sibling Of");
    newAccount.setParentId(99L);
    doReturn(Optional.empty()).when(spyAccountRepository).findById(anyLong());
    assertThrows(
        AccountNotFoundException.class,
        () -> {
          spyAccountRepository.insertAsNextSiblingOf(newAccount, newAccount);
        });
    verify(mockNodeRepository, times(0))
        .insertAsNextSiblingOf(any(Account.class), any(Account.class));
  }

  @Test
  void testUpdate_UnsupportedOperationException() {
    // exception thrown because parent id is null, indirectly testing preventUnsafeOperations()
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          spyAccountRepository.update(new Account());
        });
  }

  @Test
  void testUpdate() {
    Iterable<Account> ia = treeAsList();
    doReturn(ia).when(mockNodeRepository).getTreeAsList(any(Account.class));
    doReturn("Any Long Name").when(spyAccountRepository).deriveLongName(any(Account.class));
    spyAccountRepository.update(accountId2());
    verify(spyAccountRepository, times(3)).deriveLongName(any(Account.class));
    verify(entityManagerMock, times(1)).merge(any(Account.class));
  }

  @Test
  void testDeriveLongName() {
    List<Account> accountList = new ArrayList<>();
    accountList.add(accountId1());
    doReturn(Optional.of(accountId2())).when(spyAccountRepository).findById(anyLong());
    doReturn(accountList).when(mockNodeRepository).getParents(any(Account.class));
    assertEquals("Assets:Financial Assets", spyAccountRepository.deriveLongName(accountId7()));
  }

  @Test
  void testDeriveLongName_TreeLeftLessThanEqualToOne() {
    doReturn(Optional.of(accountId1())).when(spyAccountRepository).findById(anyLong());
    assertEquals(accountId7().getName(), spyAccountRepository.deriveLongName(accountId7()));
  }

  @Test
  void testDeriveLongName_NoParent() {
    doReturn(Optional.empty()).when(spyAccountRepository).findById(anyLong());

    assertThrows(
        AccountNotFoundException.class,
        () -> {
          spyAccountRepository.deriveLongName(accountId7());
        });
  }

  private Iterable<Account> treeAsList() {

    List<Account> treeAsList = new ArrayList<>();

    treeAsList.add(accountId2());
    treeAsList.add(accountId7());
    treeAsList.add(accountId8());

    return treeAsList;
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
