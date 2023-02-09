package net.flyingfishflash.ledger.foundation.response.advice;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;
import net.flyingfishflash.ledger.foundation.response.structure.ApplicationErrorResponse;

@RestControllerAdvice
public class AdviceForApiException {

  @ExceptionHandler(AbstractApiException.class)
  public ResponseEntity<ApplicationErrorResponse> handleApiException(
      HttpServletRequest httpServletRequest, AbstractApiException apiException) {

    var httpStatus =
        Optional.ofNullable(HttpStatus.resolve(apiException.getStatusCode().value()))
            .orElse(HttpStatus.NOT_IMPLEMENTED);

    return new ResponseEntity<>(
        new ApplicationErrorResponse(
            apiException.getApiStatusCode(),
            apiException,
            URI.create(httpServletRequest.getRequestURI())),
        httpStatus);
  }
}
