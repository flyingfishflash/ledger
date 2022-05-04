package net.flyingfishflash.ledger.importer.exceptions;

import org.springframework.http.HttpStatus;

public interface ImporterException {

  String ERROR_DOMAIN = "Importer";

  String ERROR_SUBJECT = "Gnucash Book Import";

  HttpStatus getHttpStatus();

  String getErrorDomain();

  String getErrorSubject();
}
