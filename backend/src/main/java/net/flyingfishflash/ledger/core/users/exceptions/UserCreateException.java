package net.flyingfishflash.ledger.core.users.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;

public class UserCreateException extends AbstractApiException {

  private static final String TITLE = "Problem Creating a User";

  public UserCreateException(String detail) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail);
  }

  public UserCreateException(String detail, Exception cause) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail, cause);
  }
}
