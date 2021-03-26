package net.flyingfishflash.ledger.books.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.books.data.ActiveBook;
import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.books.data.dto.SetActiveBookRequest;
import net.flyingfishflash.ledger.books.service.BookService;

@RestController
@Validated
@RequestMapping("api/v1/ledger/books")
public class BookController {

  private static final Logger logger = LoggerFactory.getLogger(BookController.class);

  private final BookService bookService;

  private final ActiveBook activeBook;

  public BookController(BookService bookService, ActiveBook activeBook) {
    this.bookService = bookService;
    this.activeBook = activeBook;
  }

  @GetMapping("{id}")
  @ResponseBody
  @Operation(summary = "Retrieve a single book")
  public Book findById(@PathVariable("id") Long id) {

    return bookService.findById(id);
  }

  @GetMapping
  @ResponseBody
  @Operation(summary = "Retrieve all books")
  public Collection<Book> findAll() {

    return bookService.findAllBooks();
  }

  @PostMapping
  @Operation(summary = "Create a new book of accounts")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public ResponseEntity<Book> bookCreate(@Valid @RequestBody BookRequest bookRequest) {

    Book newBook = bookService.createBook(bookRequest);

    return new ResponseEntity<>(newBook, HttpStatus.CREATED);
  }

  @PatchMapping("{id}")
  @ResponseBody
  @Operation(summary = "Update the properties of a single book")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public Book bookPatch(
      @PathVariable("id") Long id, @RequestBody Map<String, Object> patchRequest) {

    return bookService.patchBook(id, patchRequest);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "Delete a book and all its related objects")
  public ResponseEntity<?> bookDelete(@PathVariable("id") Long id) {

    bookService.deleteBook(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/active")
  @ResponseBody
  @Operation(summary = "Set the active book for a session")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public Book setActiveBook(@RequestBody SetActiveBookRequest setActiveBookRequest) {
    activeBook.setBookId(bookService.findById(setActiveBookRequest.getId()).getId());
    logger.info(activeBook.toString());
    return bookService.findById(activeBook.getBookId());
  }

  @GetMapping("/active")
  @ResponseBody
  @Operation(summary = "Get the active book for a session")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public Book getActiveBook() {
    return bookService.findById(activeBook.getBookId());
  }
}
