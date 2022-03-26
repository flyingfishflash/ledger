package net.flyingfishflash.ledger.importer.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPInputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.commodities.service.CommodityService;
import net.flyingfishflash.ledger.foundation.WebSocketSessionId;
import net.flyingfishflash.ledger.importer.GncXmlAccountHandler;
import net.flyingfishflash.ledger.importer.GncXmlCommodityHandler;
import net.flyingfishflash.ledger.importer.GncXmlPriceHandler;
import net.flyingfishflash.ledger.importer.GncXmlTransactionHandler;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.prices.service.PriceService;
import net.flyingfishflash.ledger.transactions.service.TransactionService;

@Service
public class GnucashFileImportService {

  private static final Logger logger = LoggerFactory.getLogger(GnucashFileImportService.class);

  @PersistenceContext EntityManager entityManager;

  private WebSocketSessionId webSocketSessionId;
  private SimpMessagingTemplate simpMessagingTemplate;
  private GnucashFileImportStatus gnucashFileImportStatus;
  private GncXmlAccountHandler gncXmlAccountHandler;
  private GncXmlCommodityHandler gncXmlCommodityHandler;
  private GncXmlPriceHandler gncXmlPriceHandler;
  private GncXmlTransactionHandler gncXmlTransactionHandler;
  private AccountService accountService;
  private CommodityService commodityService;
  private PriceService priceService;
  private TransactionService transactionService;

  public GnucashFileImportService(
      WebSocketSessionId webSocketSessionId,
      SimpMessagingTemplate simpMessagingTemplate,
      GnucashFileImportStatus gnucashFileImportStatus,
      GncXmlAccountHandler gncXmlAccountHandler,
      GncXmlCommodityHandler gncXmlCommodityHandler,
      GncXmlPriceHandler gncXmlPriceHandler,
      GncXmlTransactionHandler gncXmlTransactionHandler,
      AccountService accountService,
      CommodityService commodityService,
      PriceService priceService,
      TransactionService transactionService) {
    this.webSocketSessionId = webSocketSessionId;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
    this.gncXmlAccountHandler = gncXmlAccountHandler;
    this.gncXmlCommodityHandler = gncXmlCommodityHandler;
    this.gncXmlPriceHandler = gncXmlPriceHandler;
    this.gncXmlTransactionHandler = gncXmlTransactionHandler;
    this.accountService = accountService;
    this.commodityService = commodityService;
    this.priceService = priceService;
    this.transactionService = transactionService;
  }

  /** Parse GnuCash XML */
  public void process(InputStream gncFileInputStream)
      throws ParserConfigurationException, SAXException, IOException {

    sendImportStatusMessage("Begin Processing Gnucash XML file");

    BufferedInputStream bufferedInputStream;

    /* Check if the FileInputStream is compressed with gzip */
    var pb = new PushbackInputStream(gncFileInputStream, 2);
    var signature = new byte[2];
    var offset = 0;
    var bytesRead = 0;
    while ((bytesRead = pb.read(signature, offset, signature.length - offset)) != -1) {
      offset += bytesRead;
      if (offset >= signature.length) {
        break;
      }
    }

    pb.unread(signature);
    if (signature[0] == (byte) 0x1f && signature[1] == (byte) 0x8b) {
      bufferedInputStream = new BufferedInputStream(new GZIPInputStream(pb));
    } else {
      bufferedInputStream = new BufferedInputStream(pb);
    }

    var baos = new ByteArrayOutputStream();
    var buf = new byte[1024];
    var n = 0;
    while ((n = bufferedInputStream.read(buf)) >= 0) {
      baos.write(buf, 0, n);
    }
    bufferedInputStream.close();
    byte[] gncXmlByteArray = baos.toByteArray();

    /*
     * TODO: Error handling
     */
    logger.info("Start import");

    long startTime = System.currentTimeMillis();

    /*
     * TODO:
     *  1) Evaluate how all existing database items are deleted in the context of a book of accounts
     *     if creating a new book of accounts no deletion will be needed
     *  2) Investigate resetting the ORM sequence generators
     *
     */

    entityManager.clear();

    priceService.deleteAllPrices();
    logger.info("deleting existing prices...");
    sendImportStatusMessage("Deleting existing prices");

    transactionService.deleteAllTransactions();
    logger.info("deleting existing transactions...");
    sendImportStatusMessage("Deleting existing transactions");

    accountService.deleteAllAccounts();
    logger.info("deleting existing accounts...");
    sendImportStatusMessage("Deleting existing accounts");

    commodityService.deleteAllCommodities();
    logger.info("deleting existing commodities...");
    sendImportStatusMessage("Deleting existing commodities");

    /*
     * TODO: For some reason it's necessary to clear the persistence context after the accounts
     *  have been imported or else performance degrades significantly. Research this.
     */
    parse(gncXmlByteArray, gncXmlCommodityHandler);
    sendImportStatusMessage("Imported commodities");

    parse(gncXmlByteArray, gncXmlAccountHandler);
    sendImportStatusMessage("Imported accounts");
    entityManager.clear();

    parse(gncXmlByteArray, gncXmlTransactionHandler);
    sendImportStatusMessage("Imported transactions");
    entityManager.clear();

    parse(gncXmlByteArray, gncXmlPriceHandler);
    sendImportStatusMessage("Imported prices");
    entityManager.clear();

    long endTime = System.currentTimeMillis();
    logger.info("total import time elapsed: {}} s", (endTime - startTime) / 1000);

    gnucashFileImportStatus.setStatusComplete();
    sendImportStatusMessage("Finished");
  }

  private void parse(byte[] gncXmlByteArray, DefaultHandler handler)
      throws ParserConfigurationException, SAXException, IOException {
    long startTime = System.currentTimeMillis();
    var spf = SAXParserFactory.newInstance();
    var sp = spf.newSAXParser();
    sp.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
    sp.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
    var xr = sp.getXMLReader();
    InputStream inputStream = new ByteArrayInputStream(gncXmlByteArray);
    xr.setContentHandler(handler);
    xr.parse(new InputSource(inputStream));
    long stopTime = System.currentTimeMillis();
    logger.info("{} import time elapsed: {}} s", handler, (stopTime - startTime) / 1000);
  }

  private void sendImportStatusMessage(String message) {

    // DateTimeFormatter inBuiltFormatter1 = DateTimeFormatter.ISO_DATE_TIME;
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");

    simpMessagingTemplate.convertAndSend(
        "/import/status/messages-user" + webSocketSessionId.getSessionId(),
        formatter.format(ZonedDateTime.now()) + " -- " + message);
    this.sendImportStatusCounts();
  }

  private void sendImportStatusCounts() {
    simpMessagingTemplate.convertAndSend(
        "/import/status/counts-user" + webSocketSessionId.getSessionId(),
        "{ \"message\" : \"new counts available\" }");

    // ObjectMapper mapper = new ObjectMapper();
    // String json =
    // mapper.writerWithDefaultPrettyPrinter().writeValueAsString(gnucashFileImportStatus);

    // simpMessagingTemplate.convertAndSend(
    //    "/import/status/counts-user" + webSocketSessionId.getSessionId(), json);
  }
}
