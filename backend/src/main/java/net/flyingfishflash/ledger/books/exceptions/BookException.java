package net.flyingfishflash.ledger.books.exceptions;

import org.springframework.http.HttpStatus;

public interface BookException {

  String ERROR_DOMAIN = "Books";

  String ERROR_SUBJECT = "Book";

  HttpStatus getHttpStatus();

  String getErrorDomain();

  String getErrorSubject();
}
