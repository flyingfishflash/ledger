package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public class PriceCreateException extends GeneralPriceException {
  public PriceCreateException(String message) {
    super(message);
  }

  public PriceCreateException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
