package net.flyingfishflash.ledger.foundation.users.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public class UserNotFoundException extends AbstractApiException {

  private static final String TITLE = "User Not Found";

  public UserNotFoundException(String username) {
    super(HttpStatus.NOT_FOUND, TITLE, "User not found for user name: " + username);
  }

  public UserNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, TITLE, "User not found for id: " + id);
  }

  public UserNotFoundException(Long id, Exception cause) {
    super(HttpStatus.NOT_FOUND, TITLE, "User not found for id: " + id, cause);
  }
}
