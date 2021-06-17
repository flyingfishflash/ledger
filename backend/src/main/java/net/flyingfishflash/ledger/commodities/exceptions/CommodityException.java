package net.flyingfishflash.ledger.commodities.exceptions;

import org.springframework.http.HttpStatus;

public interface CommodityException {
  String ERROR_DOMAIN = "Commodities";

  String ERROR_SUBJECT = "Commodity";

  HttpStatus getHttpStatus();

  String getErrorDomain();

  String getErrorSubject();
}
