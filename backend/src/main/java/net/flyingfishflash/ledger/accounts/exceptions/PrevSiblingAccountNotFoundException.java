package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class PrevSiblingAccountNotFoundException extends AccountException {

  public PrevSiblingAccountNotFoundException(String accountLongName, Long id) {
    super(
        HttpStatus.NOT_FOUND,
        "Account id " + id + " (" + accountLongName + ") has no previous sibling account.");
  }
}
