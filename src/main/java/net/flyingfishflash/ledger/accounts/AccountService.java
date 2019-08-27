package net.flyingfishflash.ledger.accounts;

import java.util.Iterator;
import net.flyingfishflash.ledger.accounts.dto.CreateAccountDto;
import net.flyingfishflash.ledger.accounts.dto.AccountDto;
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
    Account sibling = new Account();
    Account parent = accountRepository.findOneById(createAccountDto.parentId);

    if (createAccountDto.siblingId != null && createAccountDto.siblingId != 0) {
      sibling = accountRepository.findOneById(createAccountDto.siblingId);
    }

    logger.debug(createAccountDto.toString());

    account.setCode(createAccountDto.code);
    account.setDescription(createAccountDto.description);
    account.setHidden(createAccountDto.hidden);
    account.setName(createAccountDto.name);
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

    return this.findByGuid(account.getGuid());
  }

  public Account findByGuid(String guid) {
    return accountRepository.findOneByGuid(guid);
  }

  public AccountDto findById(Long id) {
    Account account = accountRepository.findOneById(id);
    return new AccountDto(account);
  }

  public Iterable<Account> findAllAccounts() {

    Iterable<Account> allAccounts =
        accountRepository.getTreeAsList(accountRepository.findOneById(1L));

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
}
