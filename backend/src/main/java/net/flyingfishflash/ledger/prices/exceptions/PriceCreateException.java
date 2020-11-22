package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public class PriceCreateException extends PriceException {

  private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

  public PriceCreateException(String message) {
    super(message);
    super.setHttpStatus(httpStatus);
  }

  public PriceCreateException(String message, Exception cause) {
    super(message, cause);
    super.setHttpStatus(httpStatus);
  }
}
