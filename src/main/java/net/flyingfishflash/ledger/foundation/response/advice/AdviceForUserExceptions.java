package net.flyingfishflash.ledger.foundation.response.advice;

import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserException;
import net.flyingfishflash.ledger.foundation.users.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = UserController.class)
public class AdviceForUserExceptions extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForUserExceptions.class);

  @ExceptionHandler(value = UserException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleUserException(
      UserException exception) {

    return new ResponseEntity<>(buildErrorResponse(exception), exception.getHttpStatus());
  }

  private ErrorResponse<ErrorResponseBody> buildErrorResponse(UserException exception) {

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
