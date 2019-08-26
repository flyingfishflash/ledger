package net.flyingfishflash.ledger.service.rest;

import java.util.Iterator;
import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.CreateAccountDto;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountType;
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

  public AccountNode createAccountNode(CreateAccountDto createAccountDto) {

    AccountNode accountNode = new AccountNode();
    AccountNode sibling = new AccountNode();
    AccountNode parent = accountRepository.findOneById(createAccountDto.parentId);

    if (createAccountDto.siblingId != null && createAccountDto.siblingId != 0) {
      sibling = accountRepository.findOneById(createAccountDto.siblingId);
    }

    logger.debug(createAccountDto.toString());

    accountNode.setCode(createAccountDto.code);
    accountNode.setDescription(createAccountDto.description);
    accountNode.setHidden(createAccountDto.hidden);
    accountNode.setName(createAccountDto.name);
    accountNode.setPlaceholder(createAccountDto.placeholder);
    accountNode.setTaxRelated(createAccountDto.taxRelated);

    if (parent.getAccountCategory().equals(AccountCategory.Root)) {
      accountNode.setAccountCategory(AccountCategory.Asset);
      accountNode.setAccountType(AccountType.Asset);
    } else {
      accountNode.setAccountCategory(parent.getAccountCategory());
      accountNode.setAccountType(parent.getAccountType());
    }

    switch (createAccountDto.mode.toLowerCase()) {
      case "firstchildof":
        accountRepository.insertAsFirstChildOf(accountNode, parent);
        break;
      case "lastchildof":
        accountRepository.insertAsLastChildOf(accountNode, parent);
        break;
      case "prevsiblingof":
        accountRepository.insertAsPrevSiblingOf(accountNode, sibling);
        break;
      case "nextsiblingof":
        accountRepository.insertAsNextSiblingOf(accountNode, sibling);
        break;
      default:
        System.out.println("missing method");
    }

    return this.findByGuid(accountNode.getGuid());
  }

  public AccountNode findByGuid(String guid) {
    return accountRepository.findOneByGuid(guid);
  }

  public AccountNodeDto findById(Long id) {
    AccountNode accountNode = accountRepository.findOneById(id);
    return new AccountNodeDto(accountNode);
  }

  public Iterable<AccountNode> findAllAccounts() {

    Iterable<AccountNode> allAccounts =
        accountRepository.getTreeAsList(accountRepository.findOneById(1L));

    // remove root account
    Iterator<AccountNode> i = allAccounts.iterator();
    AccountNode a;
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
