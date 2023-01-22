package net.flyingfishflash.ledger.importer.web;

import java.io.IOException;
import java.security.Principal;

import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import net.flyingfishflash.ledger.books.exceptions.BookNotFoundException;
import net.flyingfishflash.ledger.books.service.BookService;
import net.flyingfishflash.ledger.importer.ImportingBook;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportBookRequest;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.importer.exceptions.ImportGnucashBookException;
import net.flyingfishflash.ledger.importer.service.GnucashFileImportService;

@Tag(name = "gnucash file import controller")
@RestController
@RequestMapping("api/v1/ledger/import")
public class GnucashFileImportController {

  private final BookService bookService;
  private final GnucashFileImportService gnucashFileImportService;
  private final ImportingBook importingBook;
  private final GnucashFileImportStatus gnucashFileImportStatus;

  public GnucashFileImportController(
      BookService bookService,
      GnucashFileImportService gnucashFileImportService,
      ImportingBook importingBook,
      GnucashFileImportStatus gnucashFileImportStatus) {
    this.bookService = bookService;
    this.gnucashFileImportService = gnucashFileImportService;
    this.importingBook = importingBook;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @PostMapping(
      value = "/gnucash",
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Import Gnucash file")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public ResponseEntity<GnucashFileImportStatus> gnucashFileImport(
      @RequestPart("bookId") // @Parameter(schema = @Schema(type = "string", format = "binary"))
          final String jsonBookId,
      @RequestPart("file") final MultipartFile file,
      Principal principal)
      throws ImportGnucashBookException {

    try {
      var objectMapper = new ObjectMapper();
      var gnucashFileImportBookRequest =
          objectMapper.readValue(jsonBookId, GnucashFileImportBookRequest.class);
      importingBook.setBook(bookService.findById(gnucashFileImportBookRequest.bookId()));
      gnucashFileImportService.process(file.getInputStream());
    } catch (ParserConfigurationException | IOException | SAXException exception) {
      throw new ImportGnucashBookException("Gnucash book Import error.", exception);
    } catch (BookNotFoundException exception) {
      throw new ImportGnucashBookException(
          "Gnucash book Import error. The requested book was not found.", exception);
    }
    return new ResponseEntity<>(gnucashFileImportStatus, HttpStatus.OK);
  }

  @GetMapping(value = "/gnucashFileImportStatus")
  @Operation(summary = "Status of Gnucash file import")
  public ResponseEntity<GnucashFileImportStatus> gnucashFileImportStatus() {
    return new ResponseEntity<>(this.gnucashFileImportStatus, HttpStatus.OK);
  }
}
