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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
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

    return new ResponseEntity<>(buildErrorResponse(exception), exception.getHttpStatus());
  }

  private ErrorResponse<ErrorResponseBody> buildErrorResponse(AccountException exception) {

    //String cause = (exception.getCause() != null) ? exception.getCause().toString() : null;

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(
            exception.getClass().getSimpleName(),
            exception.getLocalizedMessage(),
            exception.getErrorDomain(),
            (exception.getCause() != null) ? exception.getCause().getClass().getSimpleName() : null,
            (exception.getCause() != null) ? exception.getCause().getLocalizedMessage() : null);

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
