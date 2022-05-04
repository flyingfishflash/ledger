package net.flyingfishflash.ledger.foundation.response.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.flyingfishflash.ledger.foundation.response.structure.ApiStatusCode;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponseBody;
import net.flyingfishflash.ledger.importer.exceptions.GeneralImporterException;
import net.flyingfishflash.ledger.importer.web.GnucashFileImportController;

@Order(value = 10)
@RestControllerAdvice(assignableTypes = GnucashFileImportController.class)
public class AdviceForImporterExceptions {

  @ExceptionHandler(value = GeneralImporterException.class)
  public ResponseEntity<ErrorResponse<ErrorResponseBody>> handleUserException(
      GeneralImporterException exception) {

    return new ResponseEntity<>(
        new ErrorResponse<>(new ErrorResponseBody(exception), ApiStatusCode.FAIL),
        exception.getHttpStatus());
  }
}
