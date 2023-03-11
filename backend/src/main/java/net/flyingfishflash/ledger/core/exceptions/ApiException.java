package net.flyingfishflash.ledger.core.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends AbstractApiException {

  private static final String TITLE = "API Exception";

  public ApiException(HttpStatus httpStatus, String detail) {
    super(httpStatus, TITLE, detail);
  }

  public ApiException(HttpStatus httpStatus, String detail, Exception cause) {
    super(httpStatus, TITLE, detail, cause);
  }
}
