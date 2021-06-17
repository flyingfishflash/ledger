package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class NextSiblingAccountNotFoundException extends GeneralAccountException
    implements AccountException {

  public NextSiblingAccountNotFoundException(String accountLongName, Long accountId) {
    super("Account id " + accountId + " (" + accountLongName + ") has no next sibling account.");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
