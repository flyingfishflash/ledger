package net.flyingfishflash.ledger.accounts.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.dto.AccountDto;
import net.flyingfishflash.ledger.accounts.data.dto.CreateAccountDto;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Validated
@RequestMapping("api/v1/ledger/accounts")
public class AccountController {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping
  @ApiOperation(value = "Retrieve all accounts")
  public ResponseEntity<Collection<Account>> findAllAccounts() {

    Collection<Account> allAccounts = accountService.findAllAccounts();

    return new ResponseEntity<>(allAccounts, HttpStatus.OK);
  }

  @GetMapping(value = "{id}")
  @ApiOperation(value = "Retrieve a single account")
  public ResponseEntity<AccountDto> findAccountById(@PathVariable("id") @Min(1) Long id) {

    Account account = accountService.findById(id);
    AccountDto accountDto = new AccountDto(account);

    return new ResponseEntity<>(accountDto, HttpStatus.OK);
  }

  @PostMapping
  @ApiOperation(value = "Create a new account")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
  public ResponseEntity<AccountDto> createAccount(
      @RequestHeader(name = "X-COM-LOCATION", required = false) String headerLocation,
      @Valid @RequestBody CreateAccountDto createAccountDto)
      throws URISyntaxException {

    Account account = accountService.createAccount(createAccountDto);
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
  @ApiOperation(value = "Delete an account and all of it's descendants")
  public ResponseEntity<?> deleteAccountAndDescendents(
      @RequestParam(name = "accountId") Long accountId) {

    Account account = accountService.findById(accountId);
    accountService.removeSubTree(account);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("{id}")
  @ResponseBody
  @ApiOperation(value = "Update the details of a single account")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
  public ResponseEntity<?> patchAccount(
      @PathVariable("id") Long id, @RequestBody Map<String, Object> patchRequest) {

    /*
      if (newParent != account.getParentId() && newParent != account.getId()) {
        accountService.insertAsLastChildOf(
            account,
            accountRepository
                .findById(newParent)
                .orElseThrow(
                    () -> new IllegalArgumentException("Account Id " + newParent + " Not found")));
      } else {
        accountRepository.update(account);
      }
    */

    // return accountService.patchAccount(id, patchRequest);
    return new ResponseEntity<>("patchAccountResponse object", HttpStatus.OK);
  }

  // Change the position of an account in the hierarchy within the sibling level (down)
  @PostMapping(value = "/insert-as-next-sibling")
  @ApiOperation(
      value =
          "Change the position of an account in the hierarchy within the sibling level (move down in a list)")
  public ResponseEntity<?> insertAsNextSiblingOf(@RequestParam("id") Long id)
      throws URISyntaxException {

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
  @ApiOperation(
      value =
          "Change the position of an account in the hierarchy within the sibling level (move up in a list)")
  public ResponseEntity<?> insertAsPrevSiblingOf(@RequestParam("id") Long id)
      throws URISyntaxException {

    Account account = accountService.findById(id);
    Account sibling = accountService.getPrevSibling(account);
    accountService.insertAsPrevSiblingOf(account, sibling);

    String uriString = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    URI location = new URI(uriString.substring(0, uriString.lastIndexOf("/")) + "/" + id);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  // Retrieve a list of accounts that may be made a direct parent of a given account
  @GetMapping(value = "{id}/eligible-parent-accounts")
  @ApiOperation(
      value = "Retrieve a list of accounts that may be made a direct parent of a given account")
  public ResponseEntity<Collection<Account>> getEligibleParentAccountsOf(
      @PathVariable("id") Long id) throws URISyntaxException {

    Account account = accountService.findById(id);
    return new ResponseEntity<>(accountService.getEligibleParentAccounts(account), HttpStatus.OK);
  }
}
