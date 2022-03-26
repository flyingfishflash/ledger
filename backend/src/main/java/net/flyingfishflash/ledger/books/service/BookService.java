package net.flyingfishflash.ledger.books.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

import org.springframework.stereotype.Service;

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

    return bookRepository.save(new Book(bookRequest.getName()));
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

    var bookRequest = bookMapper.mapEntityModelToRequestModel(book);

    if (!patchRequest.isEmpty()) {
      for (Entry<String, Object> entry : patchRequest.entrySet()) {
        String change = entry.getKey();
        Object value = entry.getValue();
        if ("name".equals(change)) {
          bookRequest.setName((String) value);
        }
      }
    }

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

    return (List<Book>) bookRepository.findAll();
  }

  public Book findById(Long bookId) {

    return bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
  }
}
