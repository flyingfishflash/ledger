package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public class AccountNotFoundException extends AbstractApiException {

  private static final String TITLE = "Account Not Found";

  public AccountNotFoundException(String guid) {
    super(HttpStatus.NOT_FOUND, TITLE, "Account not found for guid '" + guid + "'");
  }

  public AccountNotFoundException(Long accountId) {
    super(HttpStatus.NOT_FOUND, TITLE, "Account not found for id " + accountId);
  }

  public AccountNotFoundException(String guid, String detail) {
    super(
        HttpStatus.NOT_FOUND,
        TITLE,
        String.format("Account not found for id %s. %s", guid, detail));
  }

  public AccountNotFoundException(Long accountId, String detail) {
    super(
        HttpStatus.NOT_FOUND,
        TITLE,
        String.format("Account not found for id %s. %s", accountId, detail));
  }
}
