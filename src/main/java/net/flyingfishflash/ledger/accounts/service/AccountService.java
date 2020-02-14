package net.flyingfishflash.ledger.accounts.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.data.dto.CreateAccountDto;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.utilities.IdentifierFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(CreateAccountDto createAccountDto) {

    Account account = new Account(IdentifierFactory.getInstance().generateIdentifier().toString());
    Account sibling;
    Account parent;
    try {
      parent = this.findById(createAccountDto.parentId);
    } catch (AccountNotFoundException e) {
      throw new AccountCreateException(
          "Failed to identify the parent account of an account to be created", e);
    }

    account.setCode(createAccountDto.code);
    account.setDescription(createAccountDto.description);
    account.setHidden(createAccountDto.hidden);
    account.setName(createAccountDto.name);
    account.setParentId(createAccountDto.parentId);
    account.setPlaceholder(createAccountDto.placeholder);
    account.setTaxRelated(createAccountDto.taxRelated);

    if (parent.getAccountCategory().equals(AccountCategory.Root)) {
      account.setAccountCategory(AccountCategory.Asset);
      account.setAccountType(AccountType.Asset);
    } else {
      account.setAccountCategory(parent.getAccountCategory());
      account.setAccountType(parent.getAccountType());
    }

    switch (createAccountDto.mode.toUpperCase()) {
      case "FIRST_CHILD":
        accountRepository.insertAsFirstChildOf(account, parent);
        break;
      case "LAST_CHILD":
        accountRepository.insertAsLastChildOf(account, parent);
        break;
      case "PREV_SIBLING":
        try {
          sibling = this.findById(createAccountDto.siblingId);
        } catch (AccountNotFoundException e) {
          throw new AccountCreateException(
              "Failed to identify the sibling account of an account to be created.", e);
        }
        accountRepository.insertAsPrevSiblingOf(account, sibling);
        break;
      case "NEXT_SIBLING":
        try {
          sibling = this.findById(createAccountDto.siblingId);
        } catch (AccountNotFoundException e) {
          throw new AccountCreateException(
              "Failed to identify the sibling account of an account to be created.", e);
        }
        accountRepository.insertAsNextSiblingOf(account, sibling);
        break;
      default:
        throw new AccountCreateException(
            "Failed to create account: '"
                + account.getName()
                + ". A valid nest node manipulator mode was not specified.");
    }

    try {
      return this.findByGuid(account.getGuid());
    } catch (AccountNotFoundException e) {
      throw new AccountCreateException("Failed to create account: '" + account.getName() + "'", e);
    }
  }

  public void deleteAllAccounts() {

    if (accountRepository.findRoot().isPresent()) {
      accountRepository.removeSubTree(accountRepository.findRoot().get());
    }
  }

  public void removeSingle(Account account) {

    accountRepository.removeSingle(account);
  }

  public void removeSubTree(Account account) {

    accountRepository.removeSubTree(account);
  }

  public Account findByGuid(String guid) {

    return accountRepository
        .findByGuid(guid)
        .orElseThrow(() -> new AccountNotFoundException(guid));
  }

  public Account findById(Long id) {

    return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  public Collection<Account> findAllAccounts() {

    Account rootAccount =
        accountRepository.findRoot().orElseThrow(() -> new AccountNotFoundException("Root Account Not Found."));
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

    return StreamSupport.stream(allAccounts.spliterator(), false).collect(Collectors.toList());
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

  public Collection<Account> getEligibleParentAccounts(Account account) {

    Account baseLevelParent = this.getBaseLevelParent(account);

    // Limit the pool of elligible accounts to those with the same base level parent,
    // so an Asset account can't become a child of a Liability Account, etc.
    Iterable<Account> eligibleParentAccounts = accountRepository.getTreeAsList(baseLevelParent);
    // Remove passed account and its children from list of eligible parent eligibleParentAccounts
    Iterator<Account> it = eligibleParentAccounts.iterator();
    while (it.hasNext()) {
      Account a = it.next();
      if (a.getTreeLeft() > account.getTreeLeft() && a.getTreeLeft() < account.getTreeRight()) {
        it.remove();
      } else if (a.getId().equals(account.getId())) {
        it.remove();
      }
    }

    long eligibleParentsCount =
        StreamSupport.stream(eligibleParentAccounts.spliterator(), false).count();

    if (eligibleParentsCount > 0) {
      //return eligibleParentAccounts;
      return StreamSupport.stream(eligibleParentAccounts.spliterator(), false)
          .collect(Collectors.toList());
    } else {
      throw new EligibleParentAccountNotFoundException(account.getId());
    }
  }
}
