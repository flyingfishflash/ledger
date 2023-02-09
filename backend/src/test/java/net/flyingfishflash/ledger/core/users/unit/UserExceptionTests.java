package net.flyingfishflash.ledger.core.users.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.core.users.exceptions.UserNotFoundException;

class UserExceptionTests {

  private final UserNotFoundException userException = new UserNotFoundException(1L);

  @Test
  void userException_getHttpStatus_isEqualTo404() {
    assertThat(userException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void userCreateException() {
    var message = "Lorem Ipsum";
    var runtimeException = new RuntimeException("Root Cause of UserCreateException");
    var userCreateException = new UserCreateException(message, runtimeException);
    assertThat(userCreateException.getCause().getLocalizedMessage())
        .isEqualTo(runtimeException.getLocalizedMessage());
  }
}
