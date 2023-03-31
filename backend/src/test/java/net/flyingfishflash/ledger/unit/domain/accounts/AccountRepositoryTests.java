package net.flyingfishflash.ledger.unit.domain.accounts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import jakarta.persistence.EntityManager;

import net.flyingfishflash.ledger.core.utilities.IdentifierUtility;
import net.flyingfishflash.ledger.domain.accounts.data.Account;
import net.flyingfishflash.ledger.domain.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.NestedNodeRepository;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.domain.books.data.Book;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryTests {

  @Mock private NestedNodeRepository<Long, Account> mockNodeRepository;
  @Spy @InjectMocks private AccountRepository spyAccountRepository;
  @MockBean EntityManager mockEntityManager;
  @InjectMocks AccountRepository accountRepository;

  @BeforeEach
  public void setup() {
    mockEntityManager = Mockito.mock(EntityManager.class);
    ReflectionTestUtils.setField(spyAccountRepository, "entityManager", mockEntityManager);
  }

  @Test
  void getTreeAsList() {
    accountRepository.getTreeAsList(new Account(), new Book());
    verify(mockNodeRepository, times(1))
        .getTreeAsList(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void getPrevSibling() {
    accountRepository.getPrevSibling(new Account(), new Book());
    verify(mockNodeRepository, times(1))
        .getPrevSibling(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void getNextSibling() {
    accountRepository.getNextSibling(new Account(), new Book());
    verify(mockNodeRepository, times(1))
        .getNextSibling(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void getParents() {
    accountRepository.getParents(new Account(), new Book());
    verify(mockNodeRepository, times(1))
        .getParents(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void removeSingle() {
    accountRepository.removeSingle(new Account(), new Book());
    verify(mockNodeRepository, times(1))
        .removeSingle(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void removeSubTree() {
    accountRepository.removeSubTree(new Account(), new Book());
    verify(mockNodeRepository, times(1))
        .removeSubtree(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void insertAsFirstRoot() {
    Long rootLevelNodeCount = 0L;
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Root Account First");
    doReturn(rootLevelNodeCount).when(spyAccountRepository).rootLevelNodeCount(newAccount);
    spyAccountRepository.insertAsFirstRoot(newAccount, new Book());
    verify(mockNodeRepository, times(1))
        .insertAsFirstRoot(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void insertAsLastRoot() {
    Long rootLevelNodeCount = 0L;
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Root Account Last");
    doReturn(rootLevelNodeCount).when(spyAccountRepository).rootLevelNodeCount(newAccount);
    spyAccountRepository.insertAsLastRoot(newAccount, new Book());
    verify(mockNodeRepository, times(1))
        .insertAsLastRoot(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void insertAsLastRoot_whenRootLevelNodeCountExceedsZero_thenAccountCreateException() {
    Long rootLevelNodeCount = 1L;
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Root Account Last");
    doReturn(rootLevelNodeCount).when(spyAccountRepository).rootLevelNodeCount(newAccount);
    assertThatExceptionOfType(AccountCreateException.class)
        .isThrownBy(() -> spyAccountRepository.insertAsLastRoot(newAccount, new Book()))
        .withMessageContaining(
            "A new root level account can't be created. Only one root level account may be present. Current root level node count: "
                + rootLevelNodeCount);
    verify(mockNodeRepository, times(0))
        .insertAsLastRoot(any(Account.class), any(JpaTreeDiscriminator.class));
  }

  @Test
  void insertAsLastChildOf() {
    String longName = "Dummy Long Name - Last Child Of";
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Last Child Of");
    doReturn(longName)
        .when(spyAccountRepository)
        .deriveLongName(any(Account.class), any(Book.class));
    spyAccountRepository.insertAsLastChildOf(newAccount, newAccount, new Book());
    verify(mockNodeRepository, times(1))
        .insertAsLastChildOf(any(Account.class), any(Account.class), any());
  }

  @Test
  void insertAsFirstChildOf() {
    String longName = "Dummy Long Name - First Child Of";
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("First Child Of");
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class), any());
    spyAccountRepository.insertAsFirstChildOf(newAccount, newAccount, new Book());
    verify(mockNodeRepository, times(1))
        .insertAsFirstChildOf(any(Account.class), any(Account.class), any());
  }

  @Test
  void insertAsPrevSiblingOf() {
    String longName = "Dummy Long Name - Prev Sibling Of";
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Prev Sibling Of");
    newAccount.setParentId(99L);
    doReturn(Optional.of(newAccount)).when(spyAccountRepository).findById(anyLong());
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class), any());
    spyAccountRepository.insertAsPrevSiblingOf(newAccount, newAccount, new Book());
    verify(mockNodeRepository, times(1))
        .insertAsPrevSiblingOf(any(Account.class), any(Account.class), any());
  }

  @Test
  void insertAsPrevSiblingOf_whenParentAccountNotFound_thenAccountNotFoundException() {
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Prev Sibling Of");
    newAccount.setParentId(99L);
    var newBook = new Book("Lorum Ipsum");
    newBook.setId(999L);
    doReturn(Optional.empty()).when(spyAccountRepository).findById(anyLong());
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(
            () -> spyAccountRepository.insertAsPrevSiblingOf(newAccount, newAccount, newBook));
    verify(mockNodeRepository, times(0))
        .insertAsPrevSiblingOf(any(Account.class), any(Account.class), any());
  }

  @Test
  void insertAsNextSiblingOf() {
    String longName = "Dummy Long Name - Next Sibling Of";
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Next Sibling Of");
    newAccount.setParentId(99L);
    var newBook = new Book("Lorum Ipsum");
    newBook.setId(999L);
    doReturn(Optional.of(newAccount)).when(spyAccountRepository).findById(anyLong());
    doReturn(longName).when(spyAccountRepository).deriveLongName(any(Account.class), any());
    spyAccountRepository.insertAsNextSiblingOf(newAccount, newAccount, newBook);
    verify(mockNodeRepository, times(1))
        .insertAsNextSiblingOf(any(Account.class), any(Account.class), any());
  }

  @Test
  void insertAsNextSiblingOf_whenParentAccountNotFound_thenAccountNotFoundException() {
    String guid = IdentifierUtility.identifier();
    Account newAccount = accountRepository.newAccount(guid);
    newAccount.setName("Next Sibling Of");
    newAccount.setParentId(99L);
    var newBook = new Book("Lorum Ipsum");
    newBook.setId(999L);
    doReturn(Optional.empty()).when(spyAccountRepository).findById(anyLong());
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(
            () -> spyAccountRepository.insertAsNextSiblingOf(newAccount, newAccount, newBook));
    verify(mockNodeRepository, times(0))
        .insertAsNextSiblingOf(any(Account.class), any(Account.class), any());
  }

  @Test
  void update() {
    Iterable<Account> ia = treeAsList();
    doReturn(ia).when(mockNodeRepository).getTreeAsList(any(Account.class), any());
    doReturn("Any Long Name").when(spyAccountRepository).deriveLongName(any(Account.class), any());
    spyAccountRepository.update(account2(), new Book());
    verify(spyAccountRepository, times(3)).deriveLongName(any(Account.class), any());
    verify(mockEntityManager, times(1)).merge(any(Account.class));
  }

  @Test
  void update_whenParentIdIsNull_thenUnsupportedOperationException() {
    var account = new Account();
    var book = new Book();
    // exception thrown because parent id is null, indirectly testing preventUnsafeOperations()
    assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(() -> spyAccountRepository.update(account, book));
  }

  @Test
  void deriveLongName() {
    List<Account> accountList = new ArrayList<>();
    accountList.add(account1());
    doReturn(Optional.of(account2())).when(spyAccountRepository).findById(anyLong());
    doReturn(accountList).when(mockNodeRepository).getParents(any(Account.class), any());
    assertThat(spyAccountRepository.deriveLongName(account7(), new Book()))
        .isEqualTo(account2().getName() + ':' + account7().getName());
  }

  @Test
  void deriveLongName_whenTreeLeftDoesntExceedOne_thenDerivedLongNameisEqualToTheAccountName() {
    var newBook = new Book();
    newBook.setId(999L);
    doReturn(Optional.of(account1())).when(spyAccountRepository).findById(anyLong());
    assertThat(spyAccountRepository.deriveLongName(account7(), newBook))
        .isEqualTo(account7().getName());
  }

  @Test
  void deriveLongName_whenParentAccountNotFound_thenAccountNotFoundException() {
    var account7 = account7();
    var book = new Book();
    book.setId(999L);
    doReturn(Optional.empty()).when(spyAccountRepository).findById(anyLong());
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(() -> spyAccountRepository.deriveLongName(account7, book));
  }

  private Iterable<Account> treeAsList() {
    List<Account> treeAsList = new ArrayList<>();
    treeAsList.add(account2());
    treeAsList.add(account7());
    treeAsList.add(account8());
    return treeAsList;
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
