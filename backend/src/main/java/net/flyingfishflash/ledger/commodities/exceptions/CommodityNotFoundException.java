package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public class CommodityNotFoundException extends GeneralCommodityException
    implements CommodityException {

  public CommodityNotFoundException(String guid) {
    super("not found for guid '" + guid + "'");
  }

  public CommodityNotFoundException(Long id) {
    super("not found for id " + id + "'");
  }

  public CommodityNotFoundException(Long id, String context) {
    super("not found for id " + id + ". Context if available: " + context);
  }

  public CommodityNotFoundException(String guid, String context) {
    super("not found for guid " + guid + ". Context if available: " + context);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
