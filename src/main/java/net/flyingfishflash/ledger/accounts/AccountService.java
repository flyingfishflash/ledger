package net.flyingfishflash.ledger.accounts;

import java.util.Iterator;
import net.flyingfishflash.ledger.accounts.dto.CreateAccountDto;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

  @Autowired private AccountRepository accountRepository;

  public Account createAccountNode(CreateAccountDto createAccountDto) {

    Account account = new Account();
    Account sibling = null;
    Account parent =
        accountRepository
            .findOneById(createAccountDto.parentId)
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        createAccountDto.parentId,
                        "Attempt to identify the parent account of an account to be created."));

    if (createAccountDto.siblingId != null) {
      sibling =
          accountRepository
              .findOneById(createAccountDto.siblingId)
              .orElseThrow(
                  () ->
                      new AccountNotFoundException(
                          createAccountDto.siblingId,
                          "Attempt to identify the sibling account of an account to be created."));
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

    switch (createAccountDto.mode.toLowerCase()) {
      case "firstchildof":
        accountRepository.insertAsFirstChildOf(account, parent);
        break;
      case "lastchildof":
        accountRepository.insertAsLastChildOf(account, parent);
        break;
      case "prevsiblingof":
        accountRepository.insertAsPrevSiblingOf(account, sibling);
        break;
      case "nextsiblingof":
        accountRepository.insertAsNextSiblingOf(account, sibling);
        break;
      default:
        System.out.println("missing method");
    }

    try {
      return this.findByGuid(account.getGuid());
    } catch (AccountNotFoundException e) {
      logger.error(
          "Failed to verify the requested account '" + account.getName() + "' was persisted.", e);
      throw new AccountCreateException("Failed to create account: '" + account.getName() + "'.", e);
    }
  }

  public Account findByGuid(String guid) {

    return accountRepository
        .findOneByGuid(guid)
        .orElseThrow(() -> new AccountNotFoundException(guid));
  }

  public Account findById(Long id) {

    return accountRepository.findOneById(id).orElseThrow(() -> new AccountNotFoundException(id));
  }

  public Iterable<Account> findAllAccounts() {

    Iterable<Account> allAccounts =
        accountRepository.getTreeAsList(
            accountRepository.findOneById(1L).orElseThrow(() -> new AccountNotFoundException(1L)));

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

    return allAccounts;
  }

  public void removeSubTree(Account account) {

    accountRepository.removeSubTree(account);
  }

  public Account getPrevSibling(Account account) {

    return accountRepository
        .getPrevSibling(account)
        .orElseThrow(() -> new PrevSiblingAccountNotFoundException((account.getLongName())));
  }

  public Account getNextSibling(Account account) {

    return accountRepository
        .getNextSibling(account)
        .orElseThrow(() -> new NextSiblingAccountNotFoundException(account.getLongName()));
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
}
