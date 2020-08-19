package net.flyingfishflash.ledger.users.exceptions;

import org.springframework.http.HttpStatus;

public class UserCreateException extends UserException {

  public UserCreateException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }

  public UserCreateException(String message, RuntimeException cause) {
    super(HttpStatus.BAD_REQUEST, message, cause);
  }
}
