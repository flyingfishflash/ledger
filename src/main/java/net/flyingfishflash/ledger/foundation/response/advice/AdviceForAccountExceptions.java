package net.flyingfishflash.ledger.foundation.response.advice;

import net.flyingfishflash.ledger.accounts.exceptions.AccountException;
import net.flyingfishflash.ledger.accounts.web.AccountCategoryController;
import net.flyingfishflash.ledger.accounts.web.AccountController;
import net.flyingfishflash.ledger.accounts.web.AccountTypeController;
import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
    assignableTypes = {
      AccountController.class,
      AccountCategoryController.class,
      AccountTypeController.class
    })
public class AdviceForAccountExceptions {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForAccountExceptions.class);

  @ExceptionHandler(AccountException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleAccountNotFoundException(
      AccountException exception) {

    return new ResponseEntity<>(buildErrorResponse(exception), exception.getHttpStatus());
  }

  private ErrorResponse<ErrorResponseBody> buildErrorResponse(AccountException exception) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(
            exception.getClass().getSimpleName(),
            exception.getLocalizedMessage(),
            exception.getErrorDomain(),
            exception.getClass().getSimpleName(),
            exception.getLocalizedMessage());

    logger.warn(
        exception.getClass().getSimpleName()
            + ": "
            + exception.getLocalizedMessage()
            + " (uniqueId: "
            + errorResponseBody.getUniqueId()
            + ")");

    logger.warn(errorResponseBody.toString());

    return new ErrorResponse<>(
        errorResponseBody, exception.getLocalizedMessage(), ResponseApiStatusCode.Fail);
  }
}
