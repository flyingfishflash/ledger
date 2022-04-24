package net.flyingfishflash.ledger.books.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.books.data.ActiveBook;
import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.books.data.dto.SetActiveBookRequest;
import net.flyingfishflash.ledger.books.service.BookService;
import net.flyingfishflash.ledger.books.web.BookController;

@ExtendWith(MockitoExtension.class)
class BookControllerTests {

  private MockMvc mvc;

  @Mock private BookService mockBookService;
  @Mock private ActiveBook mockActiveBook;
  @InjectMocks private BookController mockBookController;

  private JacksonTester<BookRequest> jsonBookRequest;
  private JacksonTester<SetActiveBookRequest> jsonSetActiveBookRequest;
  private JacksonTester<Map<String, Object>> jsonPatchRequest;

  @BeforeEach
  public void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc =
        MockMvcBuilders.standaloneSetup(mockBookController)
            // .setControllerAdvice(new AdviceForUserExceptions())
            // .setControllerAdvice(new AdviceForStandardExceptions())
            // .addFilters(new SuperHeroFilter())
            .build();
  }

  @Test
  void getBook() throws Exception {
    // TODO: mock return service object and validate its Json representation
    String pathVariable = "1";
    mvc.perform(get("/api/v1/ledger/books/" + pathVariable).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockBookService, times(1)).findById(Long.valueOf(pathVariable));
  }

  @Test
  void getBooks() throws Exception {
    // TODO: mock return service object and validate its Json representation
    mvc.perform(get("/api/v1/ledger/books").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockBookService, times(1)).findAllBooks();
  }

  @Test
  void postBooks() throws Exception {
    // TODO: mock return service object and validate its Json representation
    BookRequest bookRequest = new BookRequest("Book Name");
    mvc.perform(
            post("/api/v1/ledger/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBookRequest.write(bookRequest).getJson()))
        .andExpect(status().isCreated());
    verify(mockBookService, times(1)).createBook(any(BookRequest.class));
  }

  @Test
  void patchBook() throws Exception {
    // TODO: mock return service object and validate its Json representation
    // BookRequest bookRequest = new BookRequest("Book Name");
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("Name", "New Book Name");
    mvc.perform(
            patch("/api/v1/ledger/books/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPatchRequest.write(patchRequest).getJson()))
        .andExpect(status().isOk());
    verify(mockBookService, times(1)).patchBook(anyLong(), any());
  }

  @Test
  void deleteBook() throws Exception {
    String pathVariable = "1";
    mvc.perform(delete("/api/v1/ledger/books/" + pathVariable)).andExpect(status().isNoContent());
    verify(mockBookService, times(1)).deleteBook(Long.valueOf(pathVariable));
  }

  @Test
  void postActive() throws Exception {
    // TODO: mock return service object and validate its Json representation
    SetActiveBookRequest setActiveBookRequest = new SetActiveBookRequest();
    setActiveBookRequest.setId(1L);
    Book activeBook = new Book("Book Name");
    activeBook.setId(1L);
    given(mockBookService.findById(1L)).willReturn(activeBook);
    mvc.perform(
            post("/api/v1/ledger/books/active")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSetActiveBookRequest.write(setActiveBookRequest).getJson()))
        .andExpect(status().isOk());
    verify(mockActiveBook, times(1)).setBookId(setActiveBookRequest.getId());
  }

  @Test
  void getActive() throws Exception {
    // TODO: mock return service object and validate its Json representation
    Book activeBook = new Book("Book Name");
    given(mockBookService.findById(0L)).willReturn(activeBook);
    mvc.perform(get("/api/v1/ledger/books/active").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(mockActiveBook, times(1)).getBookId();
    verify(mockBookService, times(1)).findById(0L);
  }
}
