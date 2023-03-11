package net.flyingfishflash.ledger.unit.core.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.core.users.exceptions.UserNotFoundException;

/** Unit tests for {@link net.flyingfishflash.ledger.core.users.exceptions.UserNotFoundException} */
@DisplayName("User Exception Tests")
class UserExceptionTests {

  private final UserNotFoundException userException = new UserNotFoundException(1L);

  @Test
  void userNotFoundExceptionGetHttpStatusIsEqualTo404() {
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
