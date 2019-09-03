package net.flyingfishflash.ledger.accounts.ui;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.flyingfishflash.ledger.accounts.AccountCategory;
import net.flyingfishflash.ledger.accounts.Account;
import net.flyingfishflash.ledger.accounts.AccountRepository;
import net.flyingfishflash.ledger.accounts.AccountType;
import net.flyingfishflash.ledger.accounts.AccountTypeCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AccountServiceUI")
@Transactional
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
  private static final AccountTypeCategory atc = new AccountTypeCategory();

  @Autowired private AccountRepository accountRepository;

  public Account newAccountNode(Account p) {

    Account account = accountRepository.newAccount();

    if (p.getAccountCategory().equals(AccountCategory.Root)) {
      account.setAccountCategory(AccountCategory.Asset);
      account.setAccountType(AccountType.Asset);
    } else {
      account.setAccountCategory(p.getAccountCategory());
      account.setAccountType(p.getAccountType());
    }

    return account;
  }

  public Account findOneById(Long id) {

    return accountRepository.findOneById(id).orElseThrow(() -> new IllegalArgumentException("Account Id: " + id + " Not found"));
  }

  public Iterable<Account> findWholeTree() {

    Iterator<Account> i =
        accountRepository.getTreeAsList(accountRepository.findOneById(1L).orElseThrow(() -> new IllegalArgumentException("Account Id 1L Not found"))).iterator();
    Account a;
    while (i.hasNext()) {
      a = i.next();
      if (a.getTreeLeft() == 1) {
        i.remove();
        break;
      }

      // return (Iterable<AccountNode>) () -> i;
    }

    // return () -> i;

    return accountRepository.getTreeAsList(accountRepository.findOneById(1L).orElseThrow(() -> new IllegalArgumentException("Account Id 1L Not found")));
  }

  public Iterable<Account> getTreeAsList(Account account) {

    return accountRepository.getTreeAsList(account);
  }

  public Iterable<Account> getElligibleParentAccounts(Account account) {

    Iterable<Account> accounts = this.getTreeAsList(this.getBaseLevelParent(account));
    // Remove passed account and its children from list of eligible parent accounts
    Iterator<Account> it = accounts.iterator();
    while (it.hasNext()) {
      Account a = it.next();
      if (a.getTreeLeft() > account.getTreeLeft() && a.getTreeLeft() < account.getTreeRight()) {
        it.remove();
      } else if (a.getId() == account.getId()) {
        it.remove();
      }
    }

    return accounts;
  }

  public Optional<Account> getPrevSibling(Account account) {

    return accountRepository.getPrevSibling(account);
  }

  public Optional<Account> getNextSibling(Account account) {

    return accountRepository.getNextSibling(account);
  }

  /*
   * Each account should have one base level parent
   *
   * Return that account, or return the account passed as a parameter
   *
   * Base level account has a depth of 1.
   * Root level account has a depth of 0.
   *
   */
  public Account getBaseLevelParent(Account account) {

    Account r = new Account();

    if (account.getTreeLevel() > 1) {
      Iterable<Account> parents = accountRepository.getParents(account);
      Iterator<Account> it = parents.iterator();
      while (it.hasNext()) {
        r = it.next();
        if (r.getTreeLevel() == 1) {
          break;
        }
      }
    } else {
      r = account;
    }
    return r;
  }

  public void insertAsFirstChildOf(Account account, Account parent) {

    accountRepository.insertAsFirstChildOf(account, parent);
  }

  public void insertAsLastChildOf(Account account, Account parent) {

    accountRepository.insertAsLastChildOf(account, parent);
  }

  public void insertAsNextSiblingOf(Account account, Account parent) {

    accountRepository.insertAsNextSiblingOf(account, parent);
  }

  public void insertAsPrevSiblingOf(Account account, Account parent) {

    accountRepository.insertAsPrevSiblingOf(account, parent);
  }

  public void removeSingle(Account account) {

    accountRepository.removeSingle(account);
  }

  public void removeSubTree(Account account) {

    accountRepository.removeSubTree(account);
  }

  public List<AccountCategory> getCategories() {

    return atc.getCategories();
  }

  public List<AccountCategory> getCategoriesByType(String type) {

    return atc.getCategoriesByType(type);
  }

  public List<AccountType> getTypesByCategory(String category) {

    return atc.getTypesByCategory(category);
  }
}
