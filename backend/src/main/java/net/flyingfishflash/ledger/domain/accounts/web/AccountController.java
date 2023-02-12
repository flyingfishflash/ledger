package net.flyingfishflash.ledger.domain.accounts.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import net.flyingfishflash.ledger.domain.accounts.data.Account;
import net.flyingfishflash.ledger.domain.accounts.data.dto.AccountCreateRequest;
import net.flyingfishflash.ledger.domain.accounts.data.dto.AccountRecord;
import net.flyingfishflash.ledger.domain.accounts.data.dto.ApiMessage;
import net.flyingfishflash.ledger.domain.accounts.service.AccountService;
import net.flyingfishflash.ledger.domain.books.service.BookService;

@Tag(name = "account controller")
@RestController
@Validated
@RequestMapping("${config.application.api-v1-url-path}/accounts")
@ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
public class AccountController {

  private final AccountService accountService;
  private final BookService bookService;

  public AccountController(AccountService accountService, BookService bookService) {
    this.accountService = accountService;
    this.bookService = bookService;
  }

  @GetMapping
  @Operation(summary = "Retrieve all accounts")
  public ResponseEntity<Collection<Account>> findAllAccounts(
      @RequestParam(name = "bookId") Long bookId) {

    var activeBook = bookService.findById(bookId);
    Collection<Account> allAccounts = accountService.findAllAccounts(activeBook);

    return new ResponseEntity<>(allAccounts, HttpStatus.OK);
  }

  @GetMapping(value = "{id}")
  @Operation(summary = "Retrieve a single account")
  public ResponseEntity<AccountRecord> findAccountById(@PathVariable("id") @Min(1) Long id) {

    var account = accountService.findById(id);
    var accountRecord = accountService.mapEntityToRecord(account);

    return new ResponseEntity<>(accountRecord, HttpStatus.OK);
  }

  @PostMapping
  @Operation(summary = "Create a new account")
  public ResponseEntity<AccountRecord> createAccount(
      @RequestHeader(name = "X-COM-LOCATION", required = false) String headerLocation,
      @Valid @RequestBody AccountCreateRequest accountCreateRequest) {

    var account = accountService.createAccount(accountCreateRequest);
    var accountRecord = accountService.mapEntityToRecord(account);

    var location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(account.getId())
            .toUri();

    var headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(accountRecord, headers, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/delete")
  @Operation(summary = "Delete an account and all of it's descendants")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "No Content")})
  public ResponseEntity<ApiMessage> deleteAccountAndDescendents(
      @RequestParam(name = "accountId") Long accountId) {

    var account = accountService.findById(accountId);
    accountService.removeSubTree(account);

    return new ResponseEntity<>(
        new ApiMessage("Deleted account: " + account.getLongName()), HttpStatus.NO_CONTENT);
  }

  @PatchMapping("{id}")
  @ResponseBody
  @Operation(summary = "Update the details of a single account")
  public ResponseEntity<ApiMessage> patchAccount(
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
    return new ResponseEntity<>(
        new ApiMessage("stand in for a patchAccountResponse object"), HttpStatus.OK);
  }

  // Change the position of an account in the hierarchy within the sibling level (down)
  @SuppressWarnings("java:S1075") //  URI is handled properly
  @PostMapping(value = "/insert-as-next-sibling")
  @Operation(
      summary =
          "Change the position of an account in the hierarchy within the sibling level (move down in a list)")
  public ResponseEntity<HttpHeaders> insertAsNextSiblingOf(@RequestParam("id") Long id)
      throws URISyntaxException {

    var account = accountService.findById(id);
    var sibling = accountService.getNextSibling(account);
    accountService.insertAsNextSiblingOf(account, sibling);

    var uriString = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    var location = new URI(uriString.substring(0, uriString.lastIndexOf("/")) + "/" + id);

    var headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  // Change the position of an account in the hierarchy within the sibling level (up)
  @SuppressWarnings("java:S1075") //  URI is handled properly
  @PostMapping(value = "/insert-as-prev-sibling")
  @Operation(
      summary =
          "Change the position of an account in the hierarchy within the sibling level (move up in a list)")
  public ResponseEntity<HttpHeaders> insertAsPrevSiblingOf(@RequestParam("id") Long id)
      throws URISyntaxException {

    var account = accountService.findById(id);
    var sibling = accountService.getPrevSibling(account);
    accountService.insertAsPrevSiblingOf(account, sibling);

    var uriString = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    var location = new URI(uriString.substring(0, uriString.lastIndexOf("/")) + "/" + id);

    var headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  // Retrieve a list of accounts that may be made a direct parent of a given account
  @GetMapping(value = "{id}/eligible-parent-accounts")
  @Operation(
      description =
          "Retrieve a list of accounts that may be made a direct parent of a given account")
  public ResponseEntity<Collection<Account>> getEligibleParentAccountsOf(
      @PathVariable("id") Long id) {

    var account = accountService.findById(id);
    return new ResponseEntity<>(accountService.getEligibleParentAccounts(account), HttpStatus.OK);
  }
}
