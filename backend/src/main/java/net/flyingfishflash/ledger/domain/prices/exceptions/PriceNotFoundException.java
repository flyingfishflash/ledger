package net.flyingfishflash.ledger.domain.prices.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;

public class PriceNotFoundException extends AbstractApiException {

  private static final String TITLE = "Price Not Found";

  public PriceNotFoundException(String guid) {
    super(HttpStatus.NOT_FOUND, TITLE, "not found for guid '" + guid + "'");
  }

  public PriceNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, TITLE, "not found for id " + id + "'");
  }
}
