package net.flyingfishflash.ledger.importer.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.accounts.exceptions.AccountException;

public abstract class GeneralImporterException extends RuntimeException
    implements ImporterException {

  protected GeneralImporterException(String message) {
    super(message);
  }

  protected GeneralImporterException(String message, Exception cause) {
    super(message, cause);
  }

  @Override
  public String getErrorDomain() {
    return AccountException.ERROR_DOMAIN;
  }

  @Override
  public String getErrorSubject() {
    return AccountException.ERROR_SUBJECT;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
