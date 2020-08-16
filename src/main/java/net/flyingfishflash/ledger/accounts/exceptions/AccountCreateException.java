package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountCreateException extends AccountException {

  public AccountCreateException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

  public AccountCreateException(String message, RuntimeException cause) {
    super(HttpStatus.NOT_FOUND, message, cause);
  }
}
