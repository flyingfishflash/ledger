package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AccountException {

  private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public AccountNotFoundException(String guid) {
    super("Account not found for guid '" + guid + "'");
    super.setHttpStatus(httpStatus);
  }

  public AccountNotFoundException(Long id) {
    super("Account not found for id " + id);
    super.setHttpStatus(httpStatus);
  }

  public AccountNotFoundException(Long id, String context) {
    super("Account not found for id " + id + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }

  public AccountNotFoundException(String guid, String context) {
    super("Account not found for guid " + guid + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return super.getHttpStatus();
  }
}
