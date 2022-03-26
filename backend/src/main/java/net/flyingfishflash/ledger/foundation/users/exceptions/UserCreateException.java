package net.flyingfishflash.ledger.foundation.users.exceptions;

import org.springframework.http.HttpStatus;

public class UserCreateException extends GeneralUserException implements UserException {

  public UserCreateException(String message) {
    super(message);
  }

  public UserCreateException(String message, RuntimeException cause) {
    super(message, cause);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
