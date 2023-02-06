package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public class CommodityNotFoundException extends AbstractApiException {

  private static final String TITLE = "Commodity Not Found";

  public CommodityNotFoundException(String guid) {
    super(HttpStatus.NOT_FOUND, TITLE, "Commodity not found for guid '" + guid + "'");
  }

  public CommodityNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, TITLE, "Commodity not found for id " + id + "'");
  }
}
