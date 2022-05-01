package net.flyingfishflash.ledger.books.data.dto;

import net.flyingfishflash.ledger.books.data.Book;

/** Collection of methods mapping book related objects from one to another. */
public class BookMapper {

  /*
  public BookRequest mapEntityModelToRequestModel(Book book) {
      return new BookRequest(book.getName());
    }

  public Book mapEntityModelToResponseModel(Book user) {
      BookResponse bookResponse = new BookResponse();
      bookResponse.setId(book.getId());
      bookResponse.setEmail(book.getEmail());
      bookResponse.setFirstName(book.getFirstName());
      bookResponse.setLastName(book.getLastName());
      bookResponse.setPassword("");
      return bookResponse;
  }
  */

  /**
   * Convert a book request to a book entity
   *
   * @param bookRequest Book request
   * @param book Book entity
   */
  public void mapRequestModelToEntityModel(BookRequest bookRequest, Book book) {
    book.setName(bookRequest.name());
    // return book;
  }

  /*
  public Book mapRequestModelToResponseModel(BookRequest bookRequest) {
    BookResponse bookResponse = new BookResponse();
    bookResponse.setEmail(bookRequest.getEmail());
    bookResponse.setFirstName(bookRequest.getFirstName());
    bookResponse.setLastName(bookRequest.getLastName());
    return bookResponse;
  }
  */
}
