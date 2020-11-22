package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public class PriceNotFoundException extends PriceException {

  private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public PriceNotFoundException(String guid) {
    super("not found for guid '" + guid + "'");
    super.setHttpStatus(httpStatus);
  }

  public PriceNotFoundException(Long id) {
    super("not found for id " + id + "'");
    super.setHttpStatus(httpStatus);
  }

  public PriceNotFoundException(Long id, String context) {
    super("not found for id " + id + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }

  public PriceNotFoundException(String guid, String context) {
    super("not found for guid " + guid + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }
}
