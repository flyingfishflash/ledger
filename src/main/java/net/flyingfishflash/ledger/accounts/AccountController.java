package net.flyingfishflash.ledger.accounts;

import java.net.URI;
import net.flyingfishflash.ledger.accounts.dto.AccountDto;
import net.flyingfishflash.ledger.accounts.dto.CreateAccountDto;
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
  public ResponseEntity<Iterable<Account>> findAllAccounts() throws Exception {

    Iterable<Account> allAccounts = accountService.findAllAccounts();

    return new ResponseEntity<>(allAccounts, HttpStatus.OK);
  }

  @GetMapping(value = "{id}")
  public ResponseEntity<AccountDto> findAccountById(@PathVariable Long id) throws Exception {

    Account account = accountService.findById(id);
    AccountDto accountDto = new AccountDto(account);

    return new ResponseEntity<>(accountDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<AccountDto> createAccount(
      @RequestHeader(name = "X-COM-LOCATION", required = false) String headerLocation,
      @RequestBody CreateAccountDto createAccountDto)
      throws Exception {

    Account account = accountService.createAccountNode(createAccountDto);
    AccountDto accountDto = new AccountDto(account);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(account.getId())
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(accountDto, headers, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/delete")
  public ResponseEntity<?> deleteAccountAndDescendents(
      @RequestParam(name = "accountId") Long accountId) throws Exception {

    Account account = accountService.findById(accountId);
    accountService.removeSubTree(account);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // Change the position of an account in the hierarchy within the sibling level (down)
  @PostMapping(value = "/insert-as-next-sibling")
  public ResponseEntity<?> insertAsNextSiblingOf(@RequestParam("id") Long id) throws Exception {

    Account account = accountService.findById(id);
    Account sibling = accountService.getNextSibling(account);
    accountService.insertAsNextSiblingOf(account, sibling);

    String uriString = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    URI location = new URI(uriString.substring(0, uriString.lastIndexOf("/")) + "/" + id);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  // Change the position of an account in the hierarchy within the sibling level (up)
  @PostMapping(value = "/insert-as-prev-sibling")
  public ResponseEntity<?> insertAsPrevSiblingOf(@RequestParam("id") Long id) throws Exception {

    Account account = accountService.findById(id);
    Account sibling = accountService.getPrevSibling(account);
    accountService.insertAsPrevSiblingOf(account, sibling);

    String uriString = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    URI location = new URI(uriString.substring(0, uriString.lastIndexOf("/")) + "/" + id);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }
}
