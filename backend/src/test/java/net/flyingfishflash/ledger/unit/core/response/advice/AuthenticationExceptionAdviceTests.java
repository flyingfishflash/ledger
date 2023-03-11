package net.flyingfishflash.ledger.unit.core.response.advice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import net.flyingfishflash.ledger.core.response.advice.AuthenticationExceptionAdvice;
import net.flyingfishflash.ledger.core.response.structure.Response;

/** Unit tests for {@link AuthenticationExceptionAdvice} */
@DisplayName("AuthenticationExceptionAdvice")
class AuthenticationExceptionAdviceTests {

  private static final AuthenticationExceptionAdvice authenticationExceptionAdvice =
      new AuthenticationExceptionAdvice();

  private static final MockHttpServletRequest httpServletRequest =
      new MockHttpServletRequest("Lorem Ipsum", "lorem/ipsum");

  private static final TestAuthenticationException authenticationExceptionWithCause =
      new TestAuthenticationException(
          "Lorem Ipsum With Cause", new BadCredentialsException("Bad Credentials Exception"));

  private static List<AuthenticationException> authenticationExceptions() {
    AuthenticationException authenticationException =
        new TestAuthenticationException("Lorem Ipsum");

    List<AuthenticationException> authenticationExceptionList = new ArrayList<>(2);
    authenticationExceptionList.add(authenticationException);
    authenticationExceptionList.add(authenticationExceptionWithCause);
    return authenticationExceptionList;
  }

  @ParameterizedTest
  @MethodSource("authenticationExceptions")
  void handleExceptionReturnsEssentiallyValidResponse(
      org.springframework.security.core.AuthenticationException authenticationException) {
    ResponseEntity<?> response =
        authenticationExceptionAdvice.handleException(httpServletRequest, authenticationException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isInstanceOf(Response.class);
  }

  static class TestAuthenticationException extends AuthenticationException {

    public TestAuthenticationException(String msg) {
      super(msg);
    }

    public TestAuthenticationException(String msg, Throwable cause) {
      super(msg, cause);
    }
  }
}
