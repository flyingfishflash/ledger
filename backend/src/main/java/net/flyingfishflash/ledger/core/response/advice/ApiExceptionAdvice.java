package net.flyingfishflash.ledger.core.response.advice;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;
import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

@RestControllerAdvice
public class ApiExceptionAdvice {

  @ExceptionHandler(AbstractApiException.class)
  public ResponseEntity<Response<ProblemDetail>> handleException(
      HttpServletRequest request, AbstractApiException exception) {

    var httpMethod = request.getMethod();
    var httpStatus =
        Optional.ofNullable(HttpStatus.resolve(exception.getStatusCode().value()))
            .orElse(HttpStatus.NOT_IMPLEMENTED);

    var problemDetail = exception.getBody();
    if (problemDetail.getInstance() == null) {
      problemDetail.setInstance(URI.create(request.getRequestURI()));
    }
    ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, exception);

    return new ResponseEntity<>(
        new Response<>(problemDetail, "Hard Code this Message into the ApiException", httpMethod),
        httpStatus);
  }
}
