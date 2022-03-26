package net.flyingfishflash.ledger.foundation.users.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GeneralUserException implements UserException {

  public UserNotFoundException(String username) {
    super("User not found for user name: " + username);
  }

  public UserNotFoundException(Long id) {
    super("User not found for id: " + id);
  }

  public UserNotFoundException(Long id, Exception cause) {
    super("User not found for id: " + id, cause);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
