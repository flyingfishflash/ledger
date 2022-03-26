package net.flyingfishflash.ledger.books.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GeneralBookException extends RuntimeException implements BookException {

  protected GeneralBookException(String message) {
    super("Book" + " " + message);
  }

  protected GeneralBookException(String message, Exception cause) {
    super("Book" + " " + message, cause);
  }

  @Override
  public String getErrorDomain() {
    return BookException.ERROR_DOMAIN;
  }

  @Override
  public String getErrorSubject() {
    return BookException.ERROR_SUBJECT;
  }

  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
