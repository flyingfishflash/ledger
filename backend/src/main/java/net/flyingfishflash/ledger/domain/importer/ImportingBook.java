package net.flyingfishflash.ledger.domain.importer;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import net.flyingfishflash.ledger.domain.books.data.Book;

/**
 * TODO: This should be only be stored client side and passed through to the individual
 * account/nested node repository methods, however this will be a significant undertaking. Using a
 * session scoped bean is too fragile.
 */
@Component
@RequestScope
public class ImportingBook {

  private Book book;

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }
}
