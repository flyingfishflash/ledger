package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AccountException {

  public AccountNotFoundException(String guid) {
    super(HttpStatus.NOT_FOUND, "Account not found for guid " + guid);
  }

  public AccountNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "Account not found for id " + id);
  }

  public AccountNotFoundException(Long id, String context) {
    super(
        HttpStatus.NOT_FOUND,
        "Account not found for id " + id + ". Context if available: " + context);
  }

  public AccountNotFoundException(String guid, String context) {
    super(
        HttpStatus.NOT_FOUND,
        "Account not found for guid " + guid + ". Context if available: " + context);
  }
}
