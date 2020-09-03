package net.flyingfishflash.ledger.foundation.response.advice;

import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserException;
import net.flyingfishflash.ledger.foundation.users.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(value = 10)
@RestControllerAdvice(assignableTypes = UserController.class)
public class AdviceForUserExceptions {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForUserExceptions.class);

  @ExceptionHandler(value = UserException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleUserException(
      UserException exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ResponseApiStatusCode.Fail),
        exception.getHttpStatus());
  }
}
