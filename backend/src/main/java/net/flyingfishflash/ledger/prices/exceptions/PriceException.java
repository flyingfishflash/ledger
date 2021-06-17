package net.flyingfishflash.ledger.prices.exceptions;

import org.springframework.http.HttpStatus;

public interface PriceException {
  String ERROR_DOMAIN = "Prices";

  String ERROR_SUBJECT = "Price";

  HttpStatus getHttpStatus();

  String getErrorDomain();

  String getErrorSubject();
}
