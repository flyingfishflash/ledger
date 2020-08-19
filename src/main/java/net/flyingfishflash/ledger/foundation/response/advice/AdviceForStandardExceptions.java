package net.flyingfishflash.ledger.foundation.response.advice;

import javax.validation.ConstraintViolationException;
import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Order(value = Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class AdviceForStandardExceptions {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForStandardExceptions.class);

  private static final String errorDomain = "Core";

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleAuthenticationException(
      Exception exception) {

    return new ResponseEntity<>(
        buildErrorResponse(exception, ResponseApiStatusCode.Fail), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleContraintViolationException(
      Exception exception) {

    return new ResponseEntity<>(
        buildErrorResponse(exception, ResponseApiStatusCode.Fail), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleNoHandlerFoundException(
      Exception exception) {

    return new ResponseEntity<>(
        buildErrorResponse(exception, ResponseApiStatusCode.Fail), HttpStatus.NOT_FOUND);
  }

  // --------------------------------------------------------

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleNonSpecificExceptions(
      Exception exception) {

    return new ResponseEntity<>(
        buildErrorResponse(exception, ResponseApiStatusCode.Error),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // --------------------------------------------------------

  private ErrorResponse<ErrorResponseBody> buildErrorResponse(
      Exception exception, ResponseApiStatusCode responseApiStatusCode) {

    ErrorResponseBody errorResponseBody =
        new ErrorResponseBody(
            exception.getClass().getSimpleName(),
            exception.getLocalizedMessage(),
            errorDomain,
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
        errorResponseBody, exception.getLocalizedMessage(), responseApiStatusCode);
  }
}
