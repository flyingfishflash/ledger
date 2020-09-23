package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public class CommodityCreateException extends CommodityException {

  private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

  public CommodityCreateException(String message) {
    super(message);
    super.setHttpStatus(httpStatus);
  }

  public CommodityCreateException(String message, Exception cause) {
    super(message, cause);
    super.setHttpStatus(httpStatus);
  }
}
