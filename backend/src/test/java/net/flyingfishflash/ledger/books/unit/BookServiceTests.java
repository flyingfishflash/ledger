package net.flyingfishflash.ledger.books.unit;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolationException;

import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.BookRepository;
import net.flyingfishflash.ledger.books.data.dto.BookMapper;
import net.flyingfishflash.ledger.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.books.exceptions.BookNotFoundException;
import net.flyingfishflash.ledger.books.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

  @Mock private BookRepository mockBookRepository;
  @Mock private BookMapper mockBookMapper;
  @InjectMocks private BookService bookService;

  @Test
  void createBook() {
    given(mockBookRepository.save(any(Book.class))).willReturn(new Book());
    bookService.createBook(new BookRequest("Lorem Ipsum"));
    verify(mockBookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void saveBook() {
    given(mockBookRepository.save(any(Book.class))).willReturn(new Book());
    bookService.saveBook(new Book());
    verify(mockBookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void saveAllBooks() {
    var bookList = Collections.singletonList(new Book());
    bookService.saveAllBooks(bookList);
    verify(mockBookRepository, times(1)).saveAll(anyList());
  }

  @Test
  void updateBook() {
    bookService.updateBook(new Book());
    verify(mockBookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void patchBook() {
    given(mockBookRepository.findById(anyLong())).willReturn(Optional.of(new Book()));
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("name", "New Book Name");
    bookService.patchBook(1L, patchRequest);
    verify(mockBookRepository, times(1)).findById(anyLong());
    verify(mockBookMapper, times(1))
        .mapRequestModelToEntityModel(any(BookRequest.class), any(Book.class));
  }

  @Test
  void patchBook_whenBookRequestNameIsNull_thenConstraintViolationException() {
    given(mockBookRepository.findById(anyLong())).willReturn(Optional.of(new Book()));
    Map<String, Object> patchRequest = new HashMap<>();
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> bookService.patchBook(1L, patchRequest));
    verify(mockBookRepository, times(1)).findById(anyLong());
    verify(mockBookMapper, times(0))
        .mapRequestModelToEntityModel(any(BookRequest.class), any(Book.class));
  }

  @Test
  void deleteBook() {
    bookService.deleteBook(1L);
    verify(mockBookRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void deleteAllBooks() {
    bookService.deleteAllBooks();
    verify(mockBookRepository, times(1)).deleteAll();
  }

  @Test
  void findById() {
    given(mockBookRepository.findById(anyLong())).willReturn(Optional.of(new Book()));
    bookService.findById(1L);
    verify(mockBookRepository, times(1)).findById(anyLong());
  }

  @Test
  void findById_whenBookNotFound_thenBookNotFoundException() {
    assertThatExceptionOfType(BookNotFoundException.class)
        .isThrownBy(() -> bookService.findById(1L));
    verify(mockBookRepository, times(1)).findById(anyLong());
  }

  @Test
  void findAllBooks() {
    List<Book> bookList = new ArrayList<>(1);
    bookList.add(new Book());
    given(mockBookRepository.findAll()).willReturn(bookList);
    bookService.findAllBooks();
    verify(mockBookRepository, times(1)).findAll();
  }
}
