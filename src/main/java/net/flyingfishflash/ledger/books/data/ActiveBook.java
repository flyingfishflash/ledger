package net.flyingfishflash.ledger.books.data;

import java.io.Serializable;

public class ActiveBook implements Serializable {

  Long bookId = 0L;

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  @Override
  public String toString() {
    return "ActiveBook{" + "bookId=" + bookId + '}';
  }
}
