package net.flyingfishflash.ledger.foundation.users.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.users.exceptions.GeneralUserException;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserNotFoundException;

class UserExceptionTests {

  private final GeneralUserException userException = new UserNotFoundException(1L);

  @Test
  void testUserException_getHttpStatus() {
    assertEquals(HttpStatus.NOT_FOUND, userException.getHttpStatus());
  }

  @Test
  void testUserException_getErrorDomain() {
    assertEquals("Users", userException.getErrorDomain());
  }

  @Test
  void testUserException_getErrorSubject() {
    assertEquals("User", userException.getErrorSubject());
  }

  @Test
  void testUserCreateException() {

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
