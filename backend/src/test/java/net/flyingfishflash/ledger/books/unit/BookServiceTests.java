package net.flyingfishflash.ledger.books.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.BookRepository;
import net.flyingfishflash.ledger.books.data.dto.BookMapper;
import net.flyingfishflash.ledger.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.books.exceptions.BookNotFoundException;
import net.flyingfishflash.ledger.books.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

  @InjectMocks private BookService bookServiceMock;

  @Mock private BookRepository bookRepositoryMock;

  @Mock private BookMapper bookMapperMock;

  @Test
  void testCreateBook() {
    when(bookRepositoryMock.save(any(Book.class))).thenReturn(new Book());
    bookServiceMock.createBook(new BookRequest());
    verify(bookRepositoryMock, times(1)).save(any(Book.class));
  }

  @Test
  void testSaveBook() {
    when(bookRepositoryMock.save(any(Book.class))).thenReturn(new Book());
    bookServiceMock.saveBook(new Book());
    verify(bookRepositoryMock, times(1)).save(any(Book.class));
  }

  @Test
  void testSaveAllBooks() {
    List<Book> bookList = Collections.singletonList(new Book());
    bookServiceMock.saveAllBooks(bookList);
    verify(bookRepositoryMock, times(1)).saveAll(anyList());
  }

  @Test
  void testUpdateBook() {
    bookServiceMock.updateBook(new Book());
    verify(bookRepositoryMock, times(1)).save(any(Book.class));
  }

  @Test
  void testPatchBook() {
    when(bookMapperMock.mapEntityModelToRequestModel(any(Book.class)))
        .thenReturn(new BookRequest());

    when(bookRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Book()));

    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("name", "New Book Name");

    bookServiceMock.patchBook(1L, patchRequest);

    verify(bookRepositoryMock, times(1)).findById(anyLong());
    verify(bookMapperMock, times(1)).mapEntityModelToRequestModel(any(Book.class));
  }

  @Test
  void testPatchBook_ConstraintViolationException() {
    when(bookMapperMock.mapEntityModelToRequestModel(any(Book.class)))
        .thenReturn(new BookRequest());

    when(bookRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Book()));

    Map<String, Object> patchRequest = new HashMap<>();

    assertThrows(
        ConstraintViolationException.class,
        () -> {
          bookServiceMock.patchBook(1L, patchRequest);
        });

    verify(bookRepositoryMock, times(1)).findById(anyLong());
    verify(bookMapperMock, times(1)).mapEntityModelToRequestModel(any(Book.class));
    verify(bookMapperMock, times(0))
        .mapRequestModelToEntityModel(any(BookRequest.class), any(Book.class));
  }

  @Test
  void testDeleteBook() {
    bookServiceMock.deleteBook(1L);
    verify(bookRepositoryMock, times(1)).deleteById(anyLong());
  }

  @Test
  void testDeleteAllCommodities() {
    bookServiceMock.deleteAllBooks();
    verify(bookRepositoryMock, times(1)).deleteAll();
  }

  @Test
  void testFindById() {
    given(bookRepositoryMock.findById(anyLong())).willReturn(Optional.of(new Book()));
    bookServiceMock.findById(1L);
    verify(bookRepositoryMock, times(1)).findById(anyLong());
  }

  @Test
  void testFindById_BookNotFoundException() {

    Throwable exception =
        assertThrows(
            BookNotFoundException.class,
            () -> {
              bookServiceMock.findById(1L);
            });

    verify(bookRepositoryMock, times(1)).findById(anyLong());
    System.out.println(exception.toString());
  }

  @Test
  void testFindAllBooks() {
    List<Book> bookList = new ArrayList<>(1);
    bookList.add(new Book());
    when(bookRepositoryMock.findAll()).thenReturn(bookList);
    bookServiceMock.findAllBooks();
    verify(bookRepositoryMock, times(1)).findAll();
  }
}
