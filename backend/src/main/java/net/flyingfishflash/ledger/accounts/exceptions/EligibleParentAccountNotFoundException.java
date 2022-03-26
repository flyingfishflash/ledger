package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class EligibleParentAccountNotFoundException extends GeneralAccountException
    implements AccountException {

  public EligibleParentAccountNotFoundException(String guid) {
    super("No accounts eligible to be a direct parent of account guid " + guid);
  }

  public EligibleParentAccountNotFoundException(Long accountId) {
    super("No accounts eligible to be a direct parent of account id " + accountId);
  }

  public EligibleParentAccountNotFoundException(Long accountId, String context) {
    super("No accounts eligible to be a direct parent of account id " + accountId + ". " + context);
  }

  public EligibleParentAccountNotFoundException(String guid, String context) {
    super("No accounts eligible to be a direct parent of account guid " + guid + ". " + context);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
