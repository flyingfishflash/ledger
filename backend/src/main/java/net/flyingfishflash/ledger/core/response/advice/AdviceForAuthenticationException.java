package net.flyingfishflash.ledger.core.response.advice;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.ApiStatus;
import net.flyingfishflash.ledger.core.response.structure.ApplicationErrorResponse;

@RestControllerAdvice
public class AdviceForAuthenticationException {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApplicationErrorResponse> handleAuthenticationException(
      HttpServletRequest httpServletRequest, AuthenticationException authenticationException) {

    var httpStatus = HttpStatus.UNAUTHORIZED;
    var title = "Authentication Problem";
    var instance = URI.create(httpServletRequest.getRequestURI());
    var detail = authenticationException.getLocalizedMessage();

    return new ResponseEntity<>(
        new ApplicationErrorResponse(
            ApiStatus.FAIL, httpStatus, authenticationException, title, detail, instance),
        httpStatus);
  }
}
