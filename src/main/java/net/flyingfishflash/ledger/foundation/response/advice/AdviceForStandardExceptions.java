package net.flyingfishflash.ledger.foundation.response.advice;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBodyItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(value = 1)
@RestControllerAdvice
public class AdviceForStandardExceptions extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(AdviceForStandardExceptions.class);

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleAuthenticationException(
      Exception exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ResponseApiStatusCode.Fail),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleContraintViolationException(
      Exception exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ResponseApiStatusCode.Fail),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    String errorMessage;
    List<ErrorResponseBodyItem> responseBodyItemList = new ArrayList<>();

    if (exception.getBindingResult().hasErrors()) {

      errorMessage =
          "Validation failed for object "
              + exception.getBindingResult().getObjectName()
              + " with "
              + exception.getBindingResult().getErrorCount()
              + " errors.";

      for (ObjectError error : exception.getBindingResult().getAllErrors()) {
        FieldError fieldErrors = ((FieldError) error);
        responseBodyItemList.add(
            new ErrorResponseBodyItem(
                // "Validation",
                "[" + fieldErrors.getField() + "] " + fieldErrors.getDefaultMessage(),
                "rejected field value ["
                    + ObjectUtils.nullSafeToString(fieldErrors.getRejectedValue())
                    + "]"));
      }
    } else {
      errorMessage = null;
    }

    return new ResponseEntity<>(
        new ErrorResponse<>(
            new ErrorResponseBody(exception, errorMessage, responseBodyItemList),
            ResponseApiStatusCode.Fail),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ResponseApiStatusCode.Fail),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    String errorMessage = exception.getLocalizedMessage();

    // remove any nested exception messaging
    if (exception.getLocalizedMessage().contains("nested exception is")) {

      String[] output = exception.getLocalizedMessage().split("; ");
      errorMessage = output[0];
    }

    return new ResponseEntity<>(
        new ErrorResponse<>(
            new ErrorResponseBody(exception, errorMessage), ResponseApiStatusCode.Fail),
        HttpStatus.BAD_REQUEST);
  }
}
