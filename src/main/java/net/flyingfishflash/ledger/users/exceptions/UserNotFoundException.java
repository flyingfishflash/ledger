package net.flyingfishflash.ledger.users.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(String username) {
    super(HttpStatus.NOT_FOUND, "User not found for user name: " + username);
  }

  public UserNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "User not found for id: " + id);
  }

  public UserNotFoundException(Long id, Exception cause) {
    super(HttpStatus.NOT_FOUND, "User not found for id: " + id, cause);
  }
}
