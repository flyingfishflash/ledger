package net.flyingfishflash.ledger.unit.domain.books;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.books.data.dto.BookMapper;
import net.flyingfishflash.ledger.domain.books.data.dto.BookRequest;

class BookMapperTests {

  private final BookMapper bookMapper = new BookMapper();

  @Test
  void mapRequestModelToEntityModel() {
    var bookRequest = new BookRequest("Book Name");
    var book = new Book();
    bookMapper.mapRequestModelToEntityModel(bookRequest, book);
    assertThat(book)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdInstant")
        .isEqualTo(bookRequest);
  }
}
