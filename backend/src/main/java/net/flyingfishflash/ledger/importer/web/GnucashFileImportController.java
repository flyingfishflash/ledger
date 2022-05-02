package net.flyingfishflash.ledger.importer.web;

import java.io.IOException;
import java.security.Principal;

import javax.xml.parsers.ParserConfigurationException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.importer.service.GnucashFileImportService;

@Tag(name = "gnucash file import controller")
@RestController
@RequestMapping("api/v1/ledger/import")
public class GnucashFileImportController {

  private static final Logger logger = LoggerFactory.getLogger(GnucashFileImportController.class);

  private final GnucashFileImportService gnucashFileImportService;

  private GnucashFileImportStatus gnucashFileImportStatus;

  public GnucashFileImportController(
      GnucashFileImportService gnucashFileImportService,
      GnucashFileImportStatus gnucashFileImportStatus) {
    this.gnucashFileImportService = gnucashFileImportService;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @PostMapping(value = "/gnucash")
  @Operation(summary = "Import Gnucash file")
  public ResponseEntity<GnucashFileImportStatus> gnucashFileImport(
      @RequestParam("file") MultipartFile file, Principal principal)
      throws ParserConfigurationException, SAXException, IOException {

    // GnucashFileImportResponse gnucashFileImportResponse = new GnucashFileImportResponse();
    logger.info(file.getOriginalFilename());
    logger.info("principal name: {}", principal.getName());

    gnucashFileImportService.process(file.getInputStream());

    // gnucashFileImportResponse.setMessage("Imported " + file.getOriginalFilename());

    return new ResponseEntity<>(gnucashFileImportStatus, HttpStatus.OK);
  }

  @GetMapping(value = "/gnucashFileImportStatus")
  @Operation(summary = "Status of Gnucash file import")
  public ResponseEntity<GnucashFileImportStatus> gnucashFileImportStatus()
        /*throws JsonProcessingException*/ {

    // ObjectMapper mapper = new ObjectMapper();

    // String json =
    //    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(gnucashFileImportStatus);

    return new ResponseEntity<>(this.gnucashFileImportStatus, HttpStatus.OK);
  }
}
