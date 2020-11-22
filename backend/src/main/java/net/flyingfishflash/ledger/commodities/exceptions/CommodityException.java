package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CommodityException extends RuntimeException {

  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public CommodityException(String message) {
    super("Commodity" + " " + message);
  }

  public CommodityException(String message, Exception cause) {
    super("Commodity" + " " + message, cause);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getErrorDomain() {
    return "Commodities";
  }

  public String getErrorSubject() {
    return "Commodity";
  }
}
