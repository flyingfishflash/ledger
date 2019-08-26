package net.flyingfishflash.ledger.controller.rest;

import java.net.URI;
import net.flyingfishflash.ledger.domain.CreateAccountNodeDto;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.service.rest.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<AccountNode> createAccountNode(
      @RequestHeader(name = "X-COM-LOCATION", required = false) String headerLocation,
      @RequestBody CreateAccountNodeDto createAccountNodeDto) {

    AccountNode accountNode = accountService.createAccountNode(createAccountNodeDto);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(accountNode.getId())
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<AccountNode>(accountNode, headers, HttpStatus.CREATED);
  }
}
