package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountCreateException extends GeneralAccountException implements AccountException {

  public AccountCreateException(String message) {
    super(message);
  }

  public AccountCreateException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
