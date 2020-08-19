package net.flyingfishflash.ledger.users.exceptions;

import org.springframework.http.HttpStatus;

public abstract class UserException extends RuntimeException {

  private final HttpStatus httpStatus;

  public UserException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public UserException(HttpStatus httpStatus, String message, Exception cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getErrorDomain() {
    return "User";
  }
}
