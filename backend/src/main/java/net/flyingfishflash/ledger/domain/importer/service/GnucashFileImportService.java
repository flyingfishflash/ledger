package net.flyingfishflash.ledger.domain.importer.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import net.flyingfishflash.ledger.domain.importer.GncXmlAccountHandler;
import net.flyingfishflash.ledger.domain.importer.GncXmlCommodityHandler;
import net.flyingfishflash.ledger.domain.importer.GncXmlPriceHandler;
import net.flyingfishflash.ledger.domain.importer.GncXmlTransactionHandler;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;

@Service
public class GnucashFileImportService {

  private static final Logger logger = LoggerFactory.getLogger(GnucashFileImportService.class);

  @PersistenceContext EntityManager entityManager;

  private final GncXmlAccountHandler gncXmlAccountHandler;
  private final GncXmlCommodityHandler gncXmlCommodityHandler;
  private final GncXmlPriceHandler gncXmlPriceHandler;
  private final GncXmlTransactionHandler gncXmlTransactionHandler;
  private final GnucashFileImportStatus gnucashFileImportStatus;

  public GnucashFileImportService(
      GncXmlAccountHandler gncXmlAccountHandler,
      GncXmlCommodityHandler gncXmlCommodityHandler,
      GncXmlPriceHandler gncXmlPriceHandler,
      GncXmlTransactionHandler gncXmlTransactionHandler,
      GnucashFileImportStatus gnucashFileImportStatus) {
    this.gncXmlAccountHandler = gncXmlAccountHandler;
    this.gncXmlCommodityHandler = gncXmlCommodityHandler;
    this.gncXmlPriceHandler = gncXmlPriceHandler;
    this.gncXmlTransactionHandler = gncXmlTransactionHandler;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  /** Parse GnuCash XML */
  public void process(InputStream gncFileInputStream)
      throws ParserConfigurationException, SAXException, IOException {

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
     *  1) Investigate resetting the ORM sequence generators
     *
     * NOTE:
     *  1) Order is important.
     *  2) For some reason it's necessary to clear the persistence context after the accounts
     *  have been imported or else performance degrades significantly. Research this.
     *
     */

    parse(gncXmlByteArray, gncXmlCommodityHandler);

    parse(gncXmlByteArray, gncXmlAccountHandler);
    entityManager.clear();

    parse(gncXmlByteArray, gncXmlTransactionHandler);
    entityManager.clear();

    parse(gncXmlByteArray, gncXmlPriceHandler);
    entityManager.clear();

    long endTime = System.currentTimeMillis();
    logger.info("total import time elapsed: {}} s", (endTime - startTime) / 1000);

    gnucashFileImportStatus.setStatusComplete();
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
}
