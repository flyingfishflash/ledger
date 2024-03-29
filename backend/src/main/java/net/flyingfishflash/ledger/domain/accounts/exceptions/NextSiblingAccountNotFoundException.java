package net.flyingfishflash.ledger.domain.accounts.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;

public class NextSiblingAccountNotFoundException extends AbstractApiException {

  private static final String TITLE = "Next Sibling Account Not Found";

  public NextSiblingAccountNotFoundException(String accountLongName, Long accountId) {
    super(
        HttpStatus.NOT_FOUND,
        TITLE,
        "Account id " + accountId + " (" + accountLongName + ") has no next sibling account.");
  }
}
