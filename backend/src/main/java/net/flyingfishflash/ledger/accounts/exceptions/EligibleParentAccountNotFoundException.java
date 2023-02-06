package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public class EligibleParentAccountNotFoundException extends AbstractApiException {

  private static final String TITLE = "Eligible Parent Account Not Found";

  public EligibleParentAccountNotFoundException(String guid) {
    super(
        HttpStatus.NOT_FOUND,
        TITLE,
        "No accounts eligible to be a direct parent of account guid " + guid);
  }

  public EligibleParentAccountNotFoundException(Long accountId) {
    super(
        HttpStatus.NOT_FOUND,
        TITLE,
        "No accounts eligible to be a direct parent of account id " + accountId);
  }
}
