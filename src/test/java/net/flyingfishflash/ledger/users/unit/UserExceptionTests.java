package net.flyingfishflash.ledger.users.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.flyingfishflash.ledger.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.users.exceptions.UserException;
import net.flyingfishflash.ledger.users.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UserExceptionTests {

  private final UserException userException = new UserNotFoundException(1L);

  @Test
  public void testUserException_getHttpStatus() {
    assertEquals(HttpStatus.NOT_FOUND, userException.getHttpStatus());
  }

  @Test
  public void testUserException_getErrorDomain() {
    assertEquals("User", userException.getErrorDomain());
  }

  @Test
  public void testUserCreateException() {

    String expectedMessage = "Expected UserCreateException Message";

    RuntimeException runtimeException = new RuntimeException("Root Cause of UserCreateException");

    UserCreateException userCreateException =
        new UserCreateException(expectedMessage, runtimeException);

    assertEquals(expectedMessage, userCreateException.getLocalizedMessage());
    assertEquals(
        runtimeException.getLocalizedMessage(),
        userCreateException.getCause().getLocalizedMessage());
  }
}
