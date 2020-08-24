package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountCreateException extends AccountException {

  public AccountCreateException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }

  public AccountCreateException(String message, Exception cause) {
    super(HttpStatus.BAD_REQUEST, message, cause);
  }
}
