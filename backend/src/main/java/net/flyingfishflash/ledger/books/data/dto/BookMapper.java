package net.flyingfishflash.ledger.books.data.dto;

import net.flyingfishflash.ledger.books.data.Book;

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
