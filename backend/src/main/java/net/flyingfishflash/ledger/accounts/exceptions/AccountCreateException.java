package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public class AccountCreateException extends AbstractApiException {

  private static final String TITLE = "Problem Creating Account";

  public AccountCreateException(String detail) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail);
  }

  public AccountCreateException(String detail, Exception cause) {
    super(HttpStatus.BAD_REQUEST, TITLE, detail, cause);
  }
}
