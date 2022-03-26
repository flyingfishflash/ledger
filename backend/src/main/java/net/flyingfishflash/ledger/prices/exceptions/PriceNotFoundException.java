package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public class PriceNotFoundException extends GeneralPriceException implements PriceException {
  public PriceNotFoundException(String guid) {
    super("not found for guid '" + guid + "'");
  }

  public PriceNotFoundException(Long id) {
    super("not found for id " + id + "'");
  }

  public PriceNotFoundException(Long id, String context) {
    super("not found for id " + id + ". Context if available: " + context);
  }

  public PriceNotFoundException(String guid, String context) {
    super("not found for guid " + guid + ". Context if available: " + context);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
