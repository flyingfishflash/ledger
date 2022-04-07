package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GeneralPriceException extends RuntimeException implements PriceException {

  protected GeneralPriceException(String message) {
    super(message);
  }

  protected GeneralPriceException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public String getErrorDomain() {
    return PriceException.ERROR_DOMAIN;
  }

  @Override
  public String getErrorSubject() {
    return PriceException.ERROR_SUBJECT;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
