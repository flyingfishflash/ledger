package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public class CommodityNotFoundException extends CommodityException {

  private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public CommodityNotFoundException(String guid) {
    super("not found for guid '" + guid + "'");
    super.setHttpStatus(httpStatus);
  }

  public CommodityNotFoundException(Long id) {
    super("not found for id " + id + "'");
    super.setHttpStatus(httpStatus);
  }

  public CommodityNotFoundException(Long id, String context) {
    super("not found for id " + id + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }

  public CommodityNotFoundException(String guid, String context) {
    super("not found for guid " + guid + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }
}
