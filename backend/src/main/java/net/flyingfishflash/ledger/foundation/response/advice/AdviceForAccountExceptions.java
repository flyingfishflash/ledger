package net.flyingfishflash.ledger.foundation.response.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.flyingfishflash.ledger.accounts.exceptions.GeneralAccountException;
import net.flyingfishflash.ledger.accounts.web.AccountCategoryController;
import net.flyingfishflash.ledger.accounts.web.AccountController;
import net.flyingfishflash.ledger.accounts.web.AccountTypeController;
import net.flyingfishflash.ledger.foundation.response.structure.ApiStatusCode;
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

  @ExceptionHandler(GeneralAccountException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleAccountException(
      GeneralAccountException exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ApiStatusCode.FAIL),
        exception.getHttpStatus());
  }
}
