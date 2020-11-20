package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AccountException extends RuntimeException {

  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public AccountException(String message) {
    super(message);
  }

  public AccountException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public AccountException(String message, Exception cause) {
    super(message, cause);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getErrorDomain() {
    return "Accounts";
  }

  public String getErrorSubject() {
    return "Account";
  }
}
