package net.flyingfishflash.ledger.importer.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.commodities.service.CommodityService;
import net.flyingfishflash.ledger.importer.GncXmlAccountHandler;
import net.flyingfishflash.ledger.importer.GncXmlCommodityHandler;
import net.flyingfishflash.ledger.importer.GncXmlPriceHandler;
import net.flyingfishflash.ledger.importer.GncXmlTransactionHandler;
import net.flyingfishflash.ledger.prices.service.PriceService;
import net.flyingfishflash.ledger.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

@Service
public class GnucashFileImportService {

  private static final Logger logger = LoggerFactory.getLogger(GnucashFileImportService.class);

  @PersistenceContext EntityManager entityManager;

  private GncXmlAccountHandler gncXmlAccountHandler;
  private GncXmlCommodityHandler gncXmlCommodityHandler;
  private GncXmlPriceHandler gncXmlPriceHandler;
  private GncXmlTransactionHandler gncXmlTransactionHandler;
  private AccountService accountService;
  private CommodityService commodityService;
  private PriceService priceService;
  private TransactionService transactionService;

  public GnucashFileImportService(
      GncXmlAccountHandler gncXmlAccountHandler,
      GncXmlCommodityHandler gncXmlCommodityHandler,
      GncXmlPriceHandler gncXmlPriceHandler,
      GncXmlTransactionHandler gncXmlTransactionHandler,
      AccountService accountService,
      CommodityService commodityService,
      PriceService priceService,
      TransactionService transactionService) {
    this.gncXmlAccountHandler = gncXmlAccountHandler;
    this.gncXmlCommodityHandler = gncXmlCommodityHandler;
    this.gncXmlPriceHandler = gncXmlPriceHandler;
    this.gncXmlTransactionHandler = gncXmlTransactionHandler;
    this.accountService = accountService;
    this.commodityService = commodityService;
    this.priceService = priceService;
    this.transactionService = transactionService;
  }

  /**
   * Parse GnuCash XML
   */
  public void process(InputStream gncFileInputStream)
      throws ParserConfigurationException, SAXException, IOException {

    BufferedInputStream bufferedInputStream;

    /* Check if the FileInputStream is compressed with gzip */
    PushbackInputStream pb = new PushbackInputStream(gncFileInputStream, 2);
    byte[] signature = new byte[2];
    pb.read(signature);
    pb.unread(signature);
    if (signature[0] == (byte) 0x1f && signature[1] == (byte) 0x8b) {
      bufferedInputStream = new BufferedInputStream(new GZIPInputStream(pb));
    } else {
      bufferedInputStream = new BufferedInputStream(pb);
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    int n = 0;
    while ((n = bufferedInputStream.read(buf)) >= 0) baos.write(buf, 0, n);
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
    logger.info("deleting existing prices...");
    priceService.deleteAllPrices();
    transactionService.deleteAllTransactions();
    logger.info("deleting existing transactions...");
    accountService.deleteAllAccounts();
    logger.info("deleting existing accounts...");
    commodityService.deleteAllCommodities();
    logger.info("deleting existing commodities...");

    /*
     * TODO: For some reason it's necessary to clear the persistence context after the accounts
     *  have been imported or else performance degrades significantly. Research this.
     */
    parse(gncXmlByteArray, gncXmlCommodityHandler);
    parse(gncXmlByteArray, gncXmlAccountHandler);
    entityManager.clear();
    parse(gncXmlByteArray, gncXmlTransactionHandler);
    entityManager.clear();
    parse(gncXmlByteArray, gncXmlPriceHandler);
    entityManager.clear();

    long endTime = System.currentTimeMillis();
    logger.info(String.format("total import time elapsed: %d s", (endTime - startTime) / 1000));
    gncXmlByteArray = null;
  }

  private void parse(byte[] gncXmlByteArray, DefaultHandler handler)
      throws ParserConfigurationException, SAXException, IOException {
    long startTime = System.currentTimeMillis();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser sp = spf.newSAXParser();
    XMLReader xr = sp.getXMLReader();
    InputStream inputStream = new ByteArrayInputStream(gncXmlByteArray);
    xr.setContentHandler(handler);
    xr.parse(new InputSource(inputStream));
    long stopTime = System.currentTimeMillis();
    logger.info(
        String.format(handler + " import time elapsed: %d s", (stopTime - startTime) / 1000));
  }
}
