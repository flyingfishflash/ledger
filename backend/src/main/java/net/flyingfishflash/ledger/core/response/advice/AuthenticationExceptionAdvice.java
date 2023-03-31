package net.flyingfishflash.ledger.core.response.advice;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

@RestControllerAdvice
public class AuthenticationExceptionAdvice {

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Response<ProblemDetail>> handleException(
      HttpServletRequest request, AuthenticationException exception) {

    var httpStatus = HttpStatus.UNAUTHORIZED;
    var httpMethod = request.getMethod();

    var problemDetail =
        ProblemDetail.forStatusAndDetail(httpStatus, exception.getLocalizedMessage());
    problemDetail.setTitle("Authentication Problem");
    problemDetail.setInstance(URI.create(request.getRequestURI()));
    ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, exception);

    return new ResponseEntity<>(
        new Response<>(problemDetail, "Message about AuthenticationException", httpMethod),
        httpStatus);
  }
}
