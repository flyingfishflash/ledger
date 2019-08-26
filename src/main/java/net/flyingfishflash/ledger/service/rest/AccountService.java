package net.flyingfishflash.ledger.service.rest;

import java.util.Iterator;
import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.CreateAccountNodeDto;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

  @Autowired private AccountRepository accountRepository;

  public AccountNode createAccountNode(CreateAccountNodeDto createAccountNodeDto) {

    AccountNode accountNode = new AccountNode();
    AccountNode sibling = new AccountNode();
    AccountNode parent = accountRepository.findOneById(createAccountNodeDto.parentId);

    if (createAccountNodeDto.siblingId != null && createAccountNodeDto.siblingId != 0) {
      sibling = accountRepository.findOneById(createAccountNodeDto.siblingId);
    }

    logger.debug(createAccountNodeDto.toString());

    accountNode.setCode(createAccountNodeDto.code);
    accountNode.setDescription(createAccountNodeDto.description);
    accountNode.setHidden(createAccountNodeDto.hidden);
    accountNode.setName(createAccountNodeDto.name);
    accountNode.setPlaceholder(createAccountNodeDto.placeholder);
    accountNode.setTaxRelated(createAccountNodeDto.taxRelated);

    if (parent.getAccountCategory().equals(AccountCategory.Root)) {
      accountNode.setAccountCategory(AccountCategory.Asset);
      accountNode.setAccountType(AccountType.Asset);
    } else {
      accountNode.setAccountCategory(parent.getAccountCategory());
      accountNode.setAccountType(parent.getAccountType());
    }

    switch (createAccountNodeDto.mode.toLowerCase()) {
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

    return this.findAccountByGuid(accountNode.getGuid());
  }

  public AccountNode findAccountByGuid(String guid) {
    return accountRepository.findOneByGuid(guid);
  }

  public ResponseEntity<AccountNodeDto> findAccountById(Long id) {

    AccountNode accountNode = accountRepository.findOneById(id);
    AccountNodeDto getAccountNodeDto = new AccountNodeDto(accountNode);
    logger.debug(getAccountNodeDto.longName);
    return new ResponseEntity<AccountNodeDto>(getAccountNodeDto, HttpStatus.OK);
  }

  public ResponseEntity<Iterable<AccountNode>> findAllAccounts() {

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

    return new ResponseEntity<Iterable<AccountNode>>(allAccounts, HttpStatus.OK);
  }
}
