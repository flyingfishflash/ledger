package net.flyingfishflash.ledger.domain.commodities.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;

public class CommodityCreateException extends AbstractApiException {

  private static final String TITLE = "Problem Creating Commodity";

  public CommodityCreateException(String detail) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail);
  }

  public CommodityCreateException(String detail, Exception cause) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail, cause);
  }
}
