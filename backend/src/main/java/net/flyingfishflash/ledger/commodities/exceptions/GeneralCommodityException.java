package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GeneralCommodityException extends RuntimeException
    implements CommodityException {

  protected GeneralCommodityException(String message) {
    super(message);
  }

  protected GeneralCommodityException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public String getErrorDomain() {
    return CommodityException.ERROR_DOMAIN;
  }

  @Override
  public String getErrorSubject() {
    return CommodityException.ERROR_SUBJECT;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
