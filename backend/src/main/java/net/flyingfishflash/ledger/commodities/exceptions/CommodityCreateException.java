package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public class CommodityCreateException extends GeneralCommodityException
    implements CommodityException {

  public CommodityCreateException(String message) {
    super(message);
  }

  public CommodityCreateException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
