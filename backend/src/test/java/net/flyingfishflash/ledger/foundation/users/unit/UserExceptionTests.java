package net.flyingfishflash.ledger.foundation.users.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.users.exceptions.GeneralUserException;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserNotFoundException;

class UserExceptionTests {

  private final GeneralUserException userException = new UserNotFoundException(1L);

  @Test
  void userException_getErrorDomain_isEqualToUsers() {
    assertThat(userException.getErrorDomain()).isEqualTo("Users");
  }

  @Test
  void userException_getErrorSubject_isEqualToUser() {
    assertThat(userException.getErrorSubject()).isEqualTo("User");
  }

  @Test
  void userException_getHttpStatus_isEqualTo404() {
    assertThat(userException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void userCreateException() {
    var expectedMessage = "Expected UserCreateException Message";
    var runtimeException = new RuntimeException("Root Cause of UserCreateException");
    var userCreateException = new UserCreateException(expectedMessage, runtimeException);
    assertThat(userCreateException.getLocalizedMessage()).isEqualTo(expectedMessage);
    assertThat(userCreateException.getCause().getLocalizedMessage())
        .isEqualTo(runtimeException.getLocalizedMessage());
  }
}
