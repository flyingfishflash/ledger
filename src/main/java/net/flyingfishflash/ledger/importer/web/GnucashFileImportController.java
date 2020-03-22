package net.flyingfishflash.ledger.importer.web;

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import net.flyingfishflash.ledger.importer.service.GnucashFileImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@RestController
@RequestMapping("ledger/api/v1/import")
public class GnucashFileImportController {

  private static final Logger logger = LoggerFactory.getLogger(GnucashFileImportController.class);

  private final GnucashFileImportService gnucashFileImportService;

  public GnucashFileImportController(GnucashFileImportService gnucashFileImportService) {
    this.gnucashFileImportService = gnucashFileImportService;
  }

  @PostMapping(value = "/gnucash")
  @ApiOperation(value = "Import Gnucash file")
  public ResponseEntity<String> gnucash(@RequestParam("file") MultipartFile file)
      throws ParserConfigurationException, SAXException, IOException {

    gnucashFileImportService.process(file.getInputStream());

    return new ResponseEntity<>(
        "imported gnucash file " + file.getOriginalFilename(), HttpStatus.OK);
  }
}
