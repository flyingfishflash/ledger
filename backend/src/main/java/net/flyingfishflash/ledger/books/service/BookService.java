package net.flyingfishflash.ledger.books.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;

import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.BookRepository;
import net.flyingfishflash.ledger.books.data.dto.BookMapper;
import net.flyingfishflash.ledger.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.books.exceptions.BookNotFoundException;

@Service
@Transactional
public class BookService {

  private final BookRepository bookRepository;

  private final BookMapper bookMapper;

  public BookService(BookMapper bookMapper, BookRepository bookRepository) {
    this.bookMapper = bookMapper;
    this.bookRepository = bookRepository;
  }

  public Book createBook(BookRequest bookRequest) {

    return bookRepository.save(new Book(bookRequest.name()));
  }

  public Book saveBook(Book book) {

    return bookRepository.save(book);
  }

  public void saveAllBooks(List<Book> commodities) {

    bookRepository.saveAll(commodities);
  }

  public Book updateBook(Book book) {

    return bookRepository.save(book);
  }

  public Book patchBook(Long bookId, Map<String, Object> patchRequest) {

    var book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

    String newName = null;

    if (!patchRequest.isEmpty()) {
      for (Entry<String, Object> entry : patchRequest.entrySet()) {
        String change = entry.getKey();
        Object value = entry.getValue();
        if ("name".equals(change)) {
          newName = (String) value;
        }
      }
    }

    var bookRequest = new BookRequest(newName);

    var validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<BookRequest>> violations = validator.validate(bookRequest);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    bookMapper.mapRequestModelToEntityModel(bookRequest, book);

    return book;
  }

  public void deleteBook(Long bookId) {

    bookRepository.deleteById(bookId);
  }

  public void deleteAllBooks() {

    bookRepository.deleteAll();
  }

  public List<Book> findAllBooks() {

    return bookRepository.findAll();
  }

  public Book findById(Long bookId) {

    return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
  }
}
