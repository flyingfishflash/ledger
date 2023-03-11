package net.flyingfishflash.ledger.core.response.advice;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

@RestControllerAdvice
public class MethodArgumentTypeMismatchExceptionAdvice {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Response<ProblemDetail>> handleException(
      HttpServletRequest request, MethodArgumentTypeMismatchException exception) {

    var httpStatus = HttpStatus.BAD_REQUEST;
    var httpMethod = request.getMethod();

    var problemDetail =
        ProblemDetail.forStatusAndDetail(
            httpStatus,
            String.format(
                "Argument of Parameter '%s' was of type: %s. Expected type: %s",
                exception.getName(),
                exception.getName().getClass().getSimpleName(),
                exception.getRequiredType()));
    problemDetail.setTitle("Argument Type Mismatch");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, exception);

    return new ResponseEntity<>(
        new Response<>(
            problemDetail, "Message about MethodArgumentTypeMismatchException", httpMethod),
        httpStatus);
  }
}
