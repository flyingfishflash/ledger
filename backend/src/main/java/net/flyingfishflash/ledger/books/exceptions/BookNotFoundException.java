package net.flyingfishflash.ledger.books.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends GeneralBookException implements BookException {

  public BookNotFoundException(Long id) {
    super("not found for id " + id + "'");
  }

  public BookNotFoundException(Long id, String context) {
    super("not found for id " + id + ". Context if available: " + context);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
