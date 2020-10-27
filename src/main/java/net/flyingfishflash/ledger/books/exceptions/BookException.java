package net.flyingfishflash.ledger.books.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BookException extends RuntimeException {

  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public BookException(String message) {
    super("Book" + " " + message);
  }

  public BookException(String message, Exception cause) {
    super("Book" + " " + message, cause);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getErrorDomain() {
    return "Books";
  }

  public String getErrorSubject() {
    return "Book";
  }
}
