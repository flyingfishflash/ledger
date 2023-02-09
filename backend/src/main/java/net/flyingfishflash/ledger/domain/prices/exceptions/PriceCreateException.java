package net.flyingfishflash.ledger.domain.prices.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;

public class PriceCreateException extends AbstractApiException {

  private static final String TITLE = "Problem Creating a Price";

  public PriceCreateException(String detail) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail);
  }

  public PriceCreateException(String detail, Exception cause) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail, cause);
  }
}
