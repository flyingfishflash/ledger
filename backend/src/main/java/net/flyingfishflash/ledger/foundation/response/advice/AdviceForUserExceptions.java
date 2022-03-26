package net.flyingfishflash.ledger.foundation.response.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.flyingfishflash.ledger.foundation.response.structure.ApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import net.flyingfishflash.ledger.foundation.users.exceptions.GeneralUserException;
import net.flyingfishflash.ledger.foundation.users.web.UserController;

@Order(value = 10)
@RestControllerAdvice(assignableTypes = UserController.class)
public class AdviceForUserExceptions {

  @ExceptionHandler(value = GeneralUserException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleUserException(
      GeneralUserException exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ApiStatusCode.FAIL),
        exception.getHttpStatus());
  }
}
