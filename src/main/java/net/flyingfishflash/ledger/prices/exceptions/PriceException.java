package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public abstract class PriceException extends RuntimeException {

  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public PriceException(String message) {
    super("Price" + " " + message);
  }

  public PriceException(String message, Exception cause) {
    super("Price" + " " + message, cause);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getErrorDomain() {
    return "Prices";
  }

  public String getErrorSubject() {
    return "Price";
  }
}
