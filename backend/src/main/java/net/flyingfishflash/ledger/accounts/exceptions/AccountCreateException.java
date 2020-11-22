package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountCreateException extends AccountException {

  private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

  public AccountCreateException(String message) {
    super(message);
    super.setHttpStatus(httpStatus);
  }

  public AccountCreateException(String message, Exception cause) {
    super(message, cause);
    super.setHttpStatus(httpStatus);
  }
}
