package net.flyingfishflash.ledger.foundation.response.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.flyingfishflash.ledger.accounts.exceptions.AccountException;
import net.flyingfishflash.ledger.accounts.web.AccountCategoryController;
import net.flyingfishflash.ledger.accounts.web.AccountController;
import net.flyingfishflash.ledger.accounts.web.AccountTypeController;
import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;

@Order(value = 10)
@RestControllerAdvice(
    assignableTypes = {
      AccountController.class,
      AccountCategoryController.class,
      AccountTypeController.class
    })
public class AdviceForAccountExceptions {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForAccountExceptions.class);

  @ExceptionHandler(AccountException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleAccountException(
      AccountException exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ResponseApiStatusCode.Fail),
        exception.getHttpStatus());
  }
}
