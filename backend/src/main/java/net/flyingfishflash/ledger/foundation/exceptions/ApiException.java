package net.flyingfishflash.ledger.foundation.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends AbstractApiException {

  private static final String TITLE = "API Exception";

  public ApiException(String detail, Exception cause, HttpStatus httpStatus) {
    super(httpStatus, TITLE, detail, cause);
  }
}
