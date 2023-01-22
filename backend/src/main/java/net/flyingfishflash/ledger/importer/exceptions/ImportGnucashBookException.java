package net.flyingfishflash.ledger.importer.exceptions;

import org.springframework.http.HttpStatus;

public class ImportGnucashBookException extends GeneralImporterException
    implements ImporterException {

  public ImportGnucashBookException(String message) {
    super(message);
  }

  public ImportGnucashBookException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
