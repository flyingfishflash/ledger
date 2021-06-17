package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends GeneralAccountException implements AccountException {

  public AccountNotFoundException(String guid) {
    super("Account not found for guid '" + guid + "'");
  }

  public AccountNotFoundException(Long accountId) {
    super("Account not found for id " + accountId);
  }

  public AccountNotFoundException(Long accountId, String context) {
    super("Account not found for id " + accountId + ". Context if available: " + context);
  }

  public AccountNotFoundException(String guid, String context) {
    super("Account not found for guid " + guid + ". Context if available: " + context);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
