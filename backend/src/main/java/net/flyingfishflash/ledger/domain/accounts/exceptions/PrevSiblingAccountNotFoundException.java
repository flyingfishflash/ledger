package net.flyingfishflash.ledger.domain.accounts.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;

public class PrevSiblingAccountNotFoundException extends AbstractApiException {

  private static final String TITLE = "Previous Sibling Account Not Found";

  public PrevSiblingAccountNotFoundException(String accountLongName, Long accountId) {
    super(
        HttpStatus.NOT_FOUND,
        TITLE,
        "Account id " + accountId + " (" + accountLongName + ") has no previous sibling account.");
  }
}
