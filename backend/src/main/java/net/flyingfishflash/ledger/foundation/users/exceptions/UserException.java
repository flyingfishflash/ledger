package net.flyingfishflash.ledger.foundation.users.exceptions;

import org.springframework.http.HttpStatus;

public interface UserException {

  String ERROR_DOMAIN = "Users";

  String ERROR_SUBJECT = "User";

  HttpStatus getHttpStatus();

  String getErrorDomain();

  String getErrorSubject();
}
