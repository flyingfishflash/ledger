package net.flyingfishflash.ledger.domain.books.web;

import java.util.Collection;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.books.data.dto.ApiMessage;
import net.flyingfishflash.ledger.domain.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.domain.books.service.BookService;

@Tag(name = "book controller")
@RestController
@Validated
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/{id}")
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
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Successful Book Creation"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content()}),
      })
  public ResponseEntity<Book> bookCreate(@Valid @RequestBody BookRequest bookRequest) {

    var newBook = bookService.createBook(bookRequest);

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
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successful Book Deletion"),
      })
  public ResponseEntity<ApiMessage> bookDelete(@PathVariable("id") Long id) {

    bookService.deleteBook(id);

    return new ResponseEntity<>(new ApiMessage("Deleted book id: " + id), HttpStatus.NO_CONTENT);
  }

  //  @PostMapping("/active")
  //  @ResponseBody
  //  @Operation(summary = "Set the active book for a session")
  //  @ApiResponses(value = {@Response(responseCode = "400", description = "Bad Request")})
  //  public Book setActiveBook(@RequestBody SetActiveBookRequest setActiveBookRequest) {
  //    //    activeBook.setBookId(bookService.findById(setActiveBookRequest.id()).getId());
  //    //    logger.info("{}", activeBook);
  //    //    return bookService.findById(activeBook.getBookId());
  //    return new Book();
  //  }
  //
  //  @GetMapping("/active")
  //  @ResponseBody
  //  @Operation(summary = "Get the active book for a session")
  //  @ApiResponses(value = {@Response(responseCode = "400", description = "Bad Request")})
  //  public Book getActiveBook() {
  //    // return bookService.findById(activeBook.getBookId());
  //    return new Book();
  //  }
}
