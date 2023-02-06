package net.flyingfishflash.ledger.books.exceptions;

import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;

public final class BookNotFoundException extends AbstractApiException {

  private static final String TITLE = "Book Not Found";

  public BookNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, TITLE, "Book not found for id " + id + ".");
  }
}
