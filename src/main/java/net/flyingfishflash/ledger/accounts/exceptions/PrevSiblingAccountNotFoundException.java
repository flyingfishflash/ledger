package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PrevSiblingAccountNotFoundException extends RuntimeException {

  public PrevSiblingAccountNotFoundException(String accountLongName, Long id) {
    super("Account id " + id + " (" + accountLongName + ") has no previous sibling account.");
  }
}
