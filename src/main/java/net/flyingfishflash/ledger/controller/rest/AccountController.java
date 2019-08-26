package net.flyingfishflash.ledger.controller.rest;

import java.net.URI;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.domain.CreateAccountDto;
import net.flyingfishflash.ledger.service.rest.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("ledger/api/v1/accounts")
public class AccountController {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  @Autowired private AccountService accountService;

  @GetMapping
  public ResponseEntity<Iterable<AccountNode>> findAllAccounts() throws Throwable {
    Iterable<AccountNode> allAccounts = accountService.findAllAccounts();
    return new ResponseEntity<>(allAccounts, HttpStatus.OK);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<AccountNodeDto> findAccountById(@PathVariable Long id) throws Throwable {
    AccountNodeDto accountNodeDto = accountService.findById(id);
    return new ResponseEntity<>(accountNodeDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<AccountNode> createAccount(
      @RequestHeader(name = "X-COM-LOCATION", required = false) String headerLocation,
      @RequestBody CreateAccountDto createAccountDto) {

    AccountNode accountNode = accountService.createAccountNode(createAccountDto);

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
