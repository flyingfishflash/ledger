package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GeneralAccountException extends RuntimeException implements AccountException {

  protected GeneralAccountException(String message) {
    super(message);
  }

  protected GeneralAccountException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public String getErrorDomain() {
    return AccountException.ERROR_DOMAIN;
  }

  @Override
  public String getErrorSubject() {
    return AccountException.ERROR_SUBJECT;
  }

  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
