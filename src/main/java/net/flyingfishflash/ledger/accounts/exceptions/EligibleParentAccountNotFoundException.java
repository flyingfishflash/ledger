package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class EligibleParentAccountNotFoundException extends AccountException {

  private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public EligibleParentAccountNotFoundException(String guid) {
    super("No accounts eligible to be a direct parent of account guid " + guid);
    super.setHttpStatus(httpStatus);
  }

  public EligibleParentAccountNotFoundException(Long id) {
    super("No accounts eligible to be a direct parent of account id " + id);
    super.setHttpStatus(httpStatus);
  }

  public EligibleParentAccountNotFoundException(Long id, String context) {
    super("No accounts eligible to be a direct parent of account id " + id + ". " + context);
    super.setHttpStatus(httpStatus);
  }

  public EligibleParentAccountNotFoundException(String guid, String context) {
    super("No accounts eligible to be a direct parent of account guid " + guid + ". " + context);
    super.setHttpStatus(httpStatus);
  }
}
