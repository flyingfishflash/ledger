package net.flyingfishflash.ledger.books.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends BookException {

  private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public BookNotFoundException(Long id) {
    super("not found for id " + id + "'");
    super.setHttpStatus(httpStatus);
  }

  public BookNotFoundException(Long id, String context) {
    super("not found for id " + id + ". Context if available: " + context);
    super.setHttpStatus(httpStatus);
  }
}
