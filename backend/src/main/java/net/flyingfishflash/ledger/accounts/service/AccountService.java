package net.flyingfishflash.ledger.accounts.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.data.dto.AccountCreateRequest;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.foundation.IdentifierFactory;

@Service
@Transactional
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(AccountCreateRequest accountCreateRequest) {

    Account parent;
    try {
      parent = this.findById(accountCreateRequest.parentId());
    } catch (AccountNotFoundException e) {
      throw new AccountCreateException(
          "Failed to identify the parent account of an account to be created", e);
    }

    var account = new Account(IdentifierFactory.getInstance().generateIdentifier());
    Account sibling;

    account.setCode(accountCreateRequest.code());
    account.setDescription(accountCreateRequest.description());
    account.setHidden(accountCreateRequest.hidden());
    account.setName(accountCreateRequest.name());
    account.setParentId(accountCreateRequest.parentId());
    account.setPlaceholder(accountCreateRequest.placeholder());
    account.setTaxRelated(accountCreateRequest.taxRelated());

    if (parent.getType().equals(AccountType.ROOT)) {
      account.setType(AccountType.ASSET);
    } else {
      account.setType(parent.getType());
    }

    switch (accountCreateRequest.mode().toUpperCase()) {
      case "FIRST_CHILD" -> accountRepository.insertAsFirstChildOf(account, parent);
      case "LAST_CHILD" -> accountRepository.insertAsLastChildOf(account, parent);
      case "PREV_SIBLING" -> {
        try {
          sibling = this.findById(accountCreateRequest.siblingId());
        } catch (AccountNotFoundException e) {
          throw new AccountCreateException(
              "Failed to identify the sibling account of an account to be created.", e);
        }
        accountRepository.insertAsPrevSiblingOf(account, sibling);
      }
      case "NEXT_SIBLING" -> {
        try {
          sibling = this.findById(accountCreateRequest.siblingId());
        } catch (AccountNotFoundException e) {
          throw new AccountCreateException(
              "Failed to identify the sibling account of an account to be created.", e);
        }
        accountRepository.insertAsNextSiblingOf(account, sibling);
      }
      default -> throw new AccountCreateException(
          "Failed to create account: '"
              + account.getName()
              + "'. A valid nest node manipulator mode was not specified.");
    }

    try {
      return this.findByGuid(account.getGuid());
    } catch (AccountNotFoundException e) {
      throw new AccountCreateException("Failed to create account: '" + account.getName() + "'", e);
    }
  }
  /**
   * Create a new bare account with only the guid, category, and type set.
   *
   * <p>Category and Type are set based on the parents values. If the parent account is the Root,
   * then the new account Category and Type will be Asset. otherwise these values are set to match
   * the parent.
   *
   * <p>Currently only used by the SSR UI.
   *
   * @param p Parent account
   * @return Account
   */
  public Account createAccount(Account p) {

    var account =
        accountRepository.newAccount(IdentifierFactory.getInstance().generateIdentifier());

    if (p.getType().equals(AccountType.ROOT)) {
      account.setType(AccountType.ASSET);
    } else {
      account.setType(p.getType());
    }

    return account;
  }

  public void deleteAllAccounts() {

    accountRepository.findRoot().ifPresent(accountRepository::removeSubTree);
  }

  public void removeSingle(Account account) {

    accountRepository.removeSingle(account);
  }

  public void removeSubTree(Account account) {

    accountRepository.removeSubTree(account);
  }

  public Account findByGuid(String guid) {

    return accountRepository.findByGuid(guid).orElseThrow(() -> new AccountNotFoundException(guid));
  }

  public Account findById(Long id) {

    return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  public Collection<Account> findAllAccounts() {

    var rootAccount =
        accountRepository
            .findRoot()
            .orElseThrow(() -> new AccountNotFoundException("(Root Account Not Found)"));
    Iterable<Account> allAccounts = accountRepository.getTreeAsList(rootAccount);

    // remove root account
    Iterator<Account> i = allAccounts.iterator();
    Account a;
    while (i.hasNext()) {
      a = i.next();
      if (a.getTreeLeft() == 1) {
        i.remove();
        break;
      }
    }

    return StreamSupport.stream(allAccounts.spliterator(), false).toList();
  }

  public Account findRoot() {

    return accountRepository
        .findRoot()
        .orElseThrow(() -> new AccountNotFoundException("Root account could not be found."));
  }

  public Account getPrevSibling(Account account) {

    return accountRepository
        .getPrevSibling(account)
        .orElseThrow(
            () -> new PrevSiblingAccountNotFoundException(account.getLongName(), account.getId()));
  }

  public Account getNextSibling(Account account) {

    return accountRepository
        .getNextSibling(account)
        .orElseThrow(
            () -> new NextSiblingAccountNotFoundException(account.getLongName(), account.getId()));
  }

  public void insertAsFirstRoot(Account account) {

    accountRepository.insertAsFirstRoot(account);
  }

  public void insertAsLastRoot(Account account) {

    accountRepository.insertAsLastRoot(account);
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

    var r = new Account();

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

  public Collection<Account> getEligibleParentAccounts(Account account) {

    Account baseLevelParent = this.getBaseLevelParent(account);

    // Limit the pool of eligible accounts to those with the same base level parent,
    // so an Asset account can't become a child of a Liability Account, etc.
    Iterable<Account> eligibleParentAccounts = accountRepository.getTreeAsList(baseLevelParent);
    // Remove passed account and its children from list of eligible parent eligibleParentAccounts
    Iterator<Account> it = eligibleParentAccounts.iterator();
    while (it.hasNext()) {
      Account a = it.next();
      if ((a.getTreeLeft() > account.getTreeLeft() && a.getTreeLeft() < account.getTreeRight())
          || a.getId().equals(account.getId())) {
        it.remove();
      }
    }

    long eligibleParentsCount =
        StreamSupport.stream(eligibleParentAccounts.spliterator(), false).count();

    if (eligibleParentsCount > 0) {
      return StreamSupport.stream(eligibleParentAccounts.spliterator(), false).toList();
    } else {
      throw new EligibleParentAccountNotFoundException(account.getId());
    }
  }
}
