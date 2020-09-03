package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AccountException extends RuntimeException {

  private final HttpStatus httpStatus;

  public AccountException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public AccountException(HttpStatus httpStatus, String message, Exception cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getErrorDomain() {
    return "Accounts";
  }
}
