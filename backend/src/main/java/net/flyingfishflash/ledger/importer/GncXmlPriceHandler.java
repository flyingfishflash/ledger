package net.flyingfishflash.ledger.importer;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.flyingfishflash.ledger.importer.adapter.PriceAdapter;
import net.flyingfishflash.ledger.importer.dto.GncPrice;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;

/**
 * Handler for parsing prices found in the GnuCash XML file. The prices are passed to an adapter
 * class to be translated and persisted in the database.
 */
@Component
public class GncXmlPriceHandler extends DefaultHandler {

  private static final Logger logger = LoggerFactory.getLogger(GncXmlPriceHandler.class);

  /** Translates GncPrice objects into Price objects and persists the results */
  private final PriceAdapter priceAdapter;

  private GnucashFileImportStatus gnucashFileImportStatus;

  /** Accumulates characters between XML tags */
  StringBuilder stringBuilder = new StringBuilder();

  /** Discovered price */
  GncPrice gncPrice;

  /** Accumulates discovered prices, passed to the adapter for translation and persistence */
  List<GncPrice> gncPrices = new ArrayList<>();

  boolean inNodeCountDataPrice = false;
  boolean inNodePrice = false;
  boolean inNodePriceCommodity = false;
  boolean inNodePriceCurrency = false;
  boolean inNodePriceTime = false;

  /** Price count as indicated in <gnc:count-data cd:type=price"><</gnc:count-data> * */
  int nodeCountDataPriceCount = -1;

  /**
   * Creates a handler for handling XML stream events when parsing the XML file
   *
   * @param priceAdapter Translates GncPrice objects into Price objects and persists the results.
   * @param gnucashFileImportStatus class capturing status of gnucash xml import
   */
  public GncXmlPriceHandler(
      PriceAdapter priceAdapter, GnucashFileImportStatus gnucashFileImportStatus) {

    this.priceAdapter = priceAdapter;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @Override
  public void startElement(
      String uri, String localName, String qualifiedName, Attributes attributes)
      throws SAXException {

    switch (qualifiedName) {
      case GncXmlHelper.TAG_PRICE:
        gncPrice = new GncPrice();
        inNodePrice = true;
        break;

      case GncXmlHelper.TAG_PRICE_COMMODITY:
        inNodePriceCommodity = true;
        break;

      case GncXmlHelper.TAG_PRICE_CURRENCY:
        inNodePriceCurrency = true;
        break;

      case GncXmlHelper.TAG_PRICE_TIME:
        inNodePriceTime = true;
        break;

      case GncXmlHelper.TAG_COUNT_DATA:
        if (attributes.getValue("cd:type").equals("price")) {
          inNodeCountDataPrice = true;
        }
        break;
      default:
    }
  }

  @SuppressWarnings("java:S3776")
  @Override
  public void endElement(String uri, String localName, String qualifiedName) throws SAXException {

    String elementData = stringBuilder.toString().trim();

    if (stringBuilder.length() == 0) {
      elementData = null;
    }

    switch (qualifiedName) {
      case GncXmlHelper.TAG_COUNT_DATA:
        if (inNodeCountDataPrice) {
          nodeCountDataPriceCount = Integer.parseInt(elementData);
          if (nodeCountDataPriceCount > 0) {
            gncPrices = new ArrayList<>(nodeCountDataPriceCount);
          }
        }
        inNodeCountDataPrice = false;
        break;

      case GncXmlHelper.TAG_PRICE_ID:
        if (inNodePrice) {
          gncPrice.setGuid(elementData);
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_SPACE:
        if (inNodePriceCommodity) {
          gncPrice.setCommodityNamespace(elementData);
        } else {
          if (inNodePriceCurrency) {
            gncPrice.setCurrencyNamespace(elementData);
          }
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_ID:
        if (inNodePriceCommodity) {
          gncPrice.setCommodityId(elementData);
        } else {
          if (inNodePriceCurrency) {
            gncPrice.setCurrencyId(elementData);
          }
        }
        break;

      case GncXmlHelper.TAG_PRICE_COMMODITY:
        inNodePriceCommodity = false;
        break;

      case GncXmlHelper.TAG_PRICE_CURRENCY:
        inNodePriceCurrency = false;
        break;

      case GncXmlHelper.TAG_TS_DATE:
        if (inNodePriceTime) {
          /* These dates are not in UTC and do not indicate their time zone */
          try {
            gncPrice.setDate(new Timestamp(GncXmlHelper.parseDate(elementData)));
          } catch (ParseException e) {
            throw new SAXException("Unable to parse transaction time: " + elementData, e);
          }
        }
        break;

      case GncXmlHelper.TAG_PRICE_TIME:
        inNodePriceTime = false;
        break;

      case GncXmlHelper.TAG_PRICE_SOURCE:
        if (inNodePrice) {
          gncPrice.setSource(elementData);
        }
        break;

      case GncXmlHelper.TAG_PRICE_TYPE:
        if (inNodePrice) {
          gncPrice.setType(elementData);
        }
        break;

      case GncXmlHelper.TAG_PRICE_VALUE:
        if (inNodePrice) {
          gncPrice.setPrice(elementData);
        }
        break;

      case GncXmlHelper.TAG_PRICE:
        gncPrices.add(gncPrice);
        inNodePrice = false;
        break;
      default:
    }

    stringBuilder.setLength(0);
  }

  @Override
  public void characters(char[] chars, int start, int length) throws SAXException {

    stringBuilder.append(chars, start, length);
  }

  @Override
  public void endDocument() throws SAXException {

    super.endDocument();

    sendToAdapter();
    gncPrices = new ArrayList<>();
  }

  private void sendToAdapter() {

    logger.info("{} indicated by gnc:count-data cd:type=\"price\"", nodeCountDataPriceCount);
    logger.info("{} sent to the adapter", gncPrices.size());
    gnucashFileImportStatus.setPricesGncCount(gncPrices.size());
    gnucashFileImportStatus.setPricesSentToAdapter(nodeCountDataPriceCount);
    priceAdapter.addRecords(gncPrices);
    gncPrices = null;
  }
}
