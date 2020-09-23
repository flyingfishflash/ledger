package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class NextSiblingAccountNotFoundException extends AccountException {

  public NextSiblingAccountNotFoundException(String accountLongName, Long id) {
    super(
        HttpStatus.NOT_FOUND,
        "Account id " + id + " (" + accountLongName + ") has no next sibling account.");
  }
}
