package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public interface AccountException {

  String ERROR_DOMAIN = "Accounts";

  String ERROR_SUBJECT = "Account";

  HttpStatus getHttpStatus();

  String getErrorDomain();

  String getErrorSubject();
}
