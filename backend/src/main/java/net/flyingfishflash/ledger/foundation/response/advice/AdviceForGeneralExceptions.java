package net.flyingfishflash.ledger.foundation.response.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.flyingfishflash.ledger.foundation.response.structure.ApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;

@Order(value = Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class AdviceForGeneralExceptions {

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleNonSpecificExceptions(
      Exception exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ApiStatusCode.ERROR),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
