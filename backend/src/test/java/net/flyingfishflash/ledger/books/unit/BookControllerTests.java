package net.flyingfishflash.ledger.books.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.books.data.ActiveBook;
import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.dto.BookRequest;
import net.flyingfishflash.ledger.books.data.dto.SetActiveBookRequest;
import net.flyingfishflash.ledger.books.service.BookService;
import net.flyingfishflash.ledger.books.web.BookController;

@ExtendWith(MockitoExtension.class)
public class BookControllerTests {

  private MockMvc mvc;

  @InjectMocks private BookController bookControllerMock;

  @Mock private BookService bookServiceMock;

  @Mock private ActiveBook activeBookMock;

  private JacksonTester<BookRequest> jsonBookRequest;
  private JacksonTester<SetActiveBookRequest> jsonSetActiveBookRequest;
  private JacksonTester<Map<String, Object>> jsonPatchRequest;

  @BeforeEach
  public void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc =
        MockMvcBuilders.standaloneSetup(bookControllerMock)
            // .setControllerAdvice(new AdviceForUserExceptions())
            // .setControllerAdvice(new AdviceForStandardExceptions())
            // .addFilters(new SuperHeroFilter())
            .build();
  }

  @Test
  public void testFindById() throws Exception {

    String pathVariable = "1";

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/books/" + pathVariable).accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(bookServiceMock, times(1)).findById(Long.valueOf(pathVariable));
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  public void testFindAllBooks() throws Exception {

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/books").accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(bookServiceMock, times(1)).findAllBooks();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  public void testCreateBook() throws Exception {

    BookRequest bookRequest = new BookRequest("Book Name");

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonBookRequest.write(bookRequest).getJson()))
            .andReturn()
            .getResponse();

    System.out.println(response.getContentAsString());

    verify(bookServiceMock, times(1)).createBook(any(BookRequest.class));
    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  public void testPatchBook() throws Exception {

    BookRequest bookRequest = new BookRequest("Book Name");

    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("Name", "New Book Name");

    MockHttpServletResponse response =
        mvc.perform(
                patch("/api/v1/ledger/books/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPatchRequest.write(patchRequest).getJson()))
            .andReturn()
            .getResponse();

    verify(bookServiceMock, times(1)).patchBook(anyLong(), any());
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  public void testDeleteBook() throws Exception {

    String pathVariable = "1";

    MockHttpServletResponse response =
        mvc.perform(delete("/api/v1/ledger/books/" + pathVariable)).andReturn().getResponse();

    verify(bookServiceMock, times(1)).deleteBook(Long.valueOf(pathVariable));
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  @Test
  public void testSetActiveBook() throws Exception {

    SetActiveBookRequest setActiveBookRequest = new SetActiveBookRequest();
    setActiveBookRequest.setId(1L);

    Book activeBook = new Book("Book Name");
    activeBook.setId(1L);

    given(bookServiceMock.findById(1L)).willReturn(activeBook);

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/books/active")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonSetActiveBookRequest.write(setActiveBookRequest).getJson()))
            .andReturn()
            .getResponse();

    verify(activeBookMock, times(1)).setBookId(setActiveBookRequest.getId());
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  public void testGetActiveBook() throws Exception {

    Book activeBook = new Book("Book Name");
    given(bookServiceMock.findById(0L)).willReturn(activeBook);

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/books/active").accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(activeBookMock, times(1)).getBookId();
    verify(bookServiceMock, times(1)).findById(0L);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }
}
