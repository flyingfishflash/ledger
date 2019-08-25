package net.flyingfishflash.ledger.controller.rest;

import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.service.rest.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ledger/api/v1/accounts")
public class AccountController {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  @Autowired private AccountService accountService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Iterable<AccountNode>> findAllAccounts() throws Throwable {
    return accountService.findAllAccounts();
  }

  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public ResponseEntity<AccountNodeDto> findAccountById(@PathVariable Long id) throws Throwable {
    return accountService.findAccountById(id);
  }
}
