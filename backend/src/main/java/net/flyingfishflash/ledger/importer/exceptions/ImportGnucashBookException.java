package net.flyingfishflash.ledger.importer.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public class ImportGnucashBookException extends AbstractApiException {

  private static final String TITLE = "Problem Importing a Gnucash Book";

  public ImportGnucashBookException(String detail) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail);
  }

  public ImportGnucashBookException(String detail, Exception cause) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail, cause);
  }

  public ImportGnucashBookException(String detail, Exception cause, HttpStatus httpStatus) {
    super(httpStatus, TITLE, detail, cause);
  }
}
