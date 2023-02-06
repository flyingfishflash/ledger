package net.flyingfishflash.ledger.foundation.response.advice;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.foundation.response.structure.ApiStatus;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ApplicationErrorResponse;

@RestControllerAdvice
public class AdviceForMethodArgumentTypeMismatchException {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApplicationErrorResponse> handleMethodArgumentTypeMismatchException(
      HttpServletRequest httpServletRequest,
      MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {

    var httpStatus = HttpStatus.BAD_REQUEST;
    var title = "Argument Type Mismatch";
    var instance = URI.create(httpServletRequest.getRequestURI());
    var detail =
        String.format(
            "Argument of Parameter '%s' was of type: %s. Expected type: %s",
            methodArgumentTypeMismatchException.getName(),
            methodArgumentTypeMismatchException.getName().getClass().getSimpleName(),
            methodArgumentTypeMismatchException.getRequiredType());

    return new ResponseEntity<>(
        new ApplicationErrorResponse(
            ApiStatus.FAIL,
            httpStatus,
            methodArgumentTypeMismatchException,
            title,
            detail,
            instance),
        httpStatus);
  }
}
