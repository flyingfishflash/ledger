package net.flyingfishflash.ledger.accounts;

import java.net.URI;
import net.flyingfishflash.ledger.accounts.dto.AccountDto;
import net.flyingfishflash.ledger.accounts.dto.CreateAccountDto;
import net.flyingfishflash.ledger.utilities.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("ledger/api/v1/accounts")
public class AccountController {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  @Autowired private AccountService accountService;

  @GetMapping
  public ResponseEntity<Iterable<Account>> findAllAccounts() throws Throwable {

    Iterable<Account> allAccounts = accountService.findAllAccounts();

    return new ResponseEntity<>(allAccounts, HttpStatus.OK);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<AccountDto> findAccountById(@PathVariable Long id) throws Throwable {

    Account account = accountService.findById(id);
    AccountDto accountDto = new AccountDto(account);

    return new ResponseEntity<>(accountDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Account> createAccount(
      @RequestHeader(name = "X-COM-LOCATION", required = false) String headerLocation,
      @RequestBody CreateAccountDto createAccountDto) throws Throwable{

    Account account = accountService.createAccountNode(createAccountDto);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(account.getId())
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<Account>(account, headers, HttpStatus.CREATED);
  }

  // Delete account and descendants
  @DeleteMapping(value = "/delete")
  public ResponseEntity<?> deleteAccountAndDescendents(@RequestParam(name = "accountId") Long accountId)
      throws Throwable {

    Account account = accountService.findById(accountId);

/*    if (account == null) {
      logger.error("Unable to delete account. Id {} not found.", accountId);
      return new ResponseEntity<>(
          new CustomErrorType("Unable to delete account. Id " + accountId + " not found."),
          HttpStatus.NOT_FOUND);
    }*/

    accountService.removeSubTree(account);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
