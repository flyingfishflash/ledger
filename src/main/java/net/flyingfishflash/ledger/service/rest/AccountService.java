package net.flyingfishflash.ledger.service.rest;

import java.util.Iterator;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.domain.AccountRepository;
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
