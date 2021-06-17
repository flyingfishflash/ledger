package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class PrevSiblingAccountNotFoundException extends GeneralAccountException
    implements AccountException {

  public PrevSiblingAccountNotFoundException(String accountLongName, Long accountId) {
    super(
        "Account id " + accountId + " (" + accountLongName + ") has no previous sibling account.");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
