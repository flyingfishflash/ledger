package net.flyingfishflash.ledger.foundation.response.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;

@Order(value = Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class AdviceForGeneralExceptions {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForStandardExceptions.class);

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleNonSpecificExceptions(
      Exception exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ResponseApiStatusCode.Error),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
