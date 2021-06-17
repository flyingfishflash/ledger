package net.flyingfishflash.ledger.foundation.users.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GeneralUserException extends RuntimeException implements UserException {

  protected GeneralUserException(String message) {
    super(message);
  }

  protected GeneralUserException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public String getErrorDomain() {
    return UserException.ERROR_DOMAIN;
  }

  @Override
  public String getErrorSubject() {
    return UserException.ERROR_SUBJECT;
  }

  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
