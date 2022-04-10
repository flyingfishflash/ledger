package net.flyingfishflash.ledger.importer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.flyingfishflash.ledger.importer.adapter.CommodityAdapter;
import net.flyingfishflash.ledger.importer.dto.GncCommodity;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;

/**
 * Handler for parsing commodity elements found in the GnuCash XML file. The discovered commodities
 * are passed to an adapter class to be translated and persisted in the database.
 */
@Component
public class GncXmlCommodityHandler extends DefaultHandler {

  private static final Logger logger = LoggerFactory.getLogger(GncXmlCommodityHandler.class);

  /** Translates GncCommodity objects -> Commodity objects and persists the results */
  private final CommodityAdapter commodityAdapter;

  private GnucashFileImportStatus gnucashFileImportStatus;

  /** Accumulates characters between XML tags */
  StringBuilder stringBuilder = new StringBuilder();

  /** Discovered commodity */
  GncCommodity gncCommodity;

  /** Accumulates discovered commodities */
  List<GncCommodity> gncCommodities = new ArrayList<>();

  boolean inNodeCountDataCommodity;
  boolean inNodeCommodity = false;
  boolean inSlotCommodityUserSymbol;

  int nodeCountDataCommodityCount = -1;

  /** Stores the attribute of the slot tag */
  String slotTagAttribute = null;

  /**
   * Creates a handler for handling XML stream events when parsing the XML file
   *
   * @param commodityAdapter translates GncCommodity objects -> Commodity objects and persists the
   *     results.
   * @param gnucashFileImportStatus class capturing status of gnucash xml import
   */
  public GncXmlCommodityHandler(
      CommodityAdapter commodityAdapter, GnucashFileImportStatus gnucashFileImportStatus) {

    this.commodityAdapter = commodityAdapter;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @Override
  public void startElement(
      String uri, String localName, String qualifiedName, Attributes attributes)
      throws SAXException {

    switch (qualifiedName) {
      case GncXmlHelper.TAG_COMMODITY:
        gncCommodity = new GncCommodity();
        inNodeCommodity = true;
        break;

      case GncXmlHelper.TAG_COUNT_DATA:
        if (attributes.getValue("cd:type").equals("commodity")) {
          inNodeCountDataCommodity = true;
        }
        break;

      case GncXmlHelper.TAG_SLOT_VALUE:
        slotTagAttribute = attributes.getValue(GncXmlHelper.ATTR_KEY_TYPE);
        break;

      default:
    }
  }

  @SuppressWarnings("java:S3776")
  @Override
  public void endElement(String uri, String localName, String qualifiedName) throws SAXException {

    var characterString = stringBuilder.toString().trim();

    if (stringBuilder.length() == 0) {
      characterString = null;
    }

    /*
     * Initialize the gncCommodities collection with a size equal to the number of items indicated
     * in the XML
     */
    switch (qualifiedName) {
      case GncXmlHelper.TAG_COUNT_DATA:
        if (inNodeCountDataCommodity) {
          nodeCountDataCommodityCount = Integer.parseInt(characterString);
          gnucashFileImportStatus.setCommoditiesGncCount(nodeCountDataCommodityCount);
          logger.info(
              "{} indicated by gnc:count-data cd:type=\"commodity\" (does not include templates)",
              nodeCountDataCommodityCount);
          if (nodeCountDataCommodityCount > 0) {
            gncCommodities = new ArrayList<>(nodeCountDataCommodityCount);
          }
        }
        inNodeCountDataCommodity = false;
        break;

      case GncXmlHelper.TAG_COMMODITY_SPACE:
        if (inNodeCommodity) {
          gncCommodity.setNamespace(characterString);
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_ID:
        if (inNodeCommodity) {
          gncCommodity.setMnemonic(characterString);
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_NAME:
        if (inNodeCommodity) {
          gncCommodity.setFullName(characterString);
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_XCODE:
        if (inNodeCommodity) {

          gncCommodity.setCusip(characterString);
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_FRACTION:
        if (inNodeCommodity) {
          // TODO: Handle potential null value
          gncCommodity.setSmallestFraction(Integer.parseInt(characterString));
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_GET_QUOTES:
        /* TODO: Handle potential NullPointerException */
        if (!"currency".equals(characterString)) {
          gncCommodity.setQuoteRemote(true);
        }
        break;

      case GncXmlHelper.TAG_SLOT_KEY:
        if (GncXmlHelper.KEY_USER_SYMBOL.equals(characterString)) {
          inSlotCommodityUserSymbol = true;
        }
        break;

      case GncXmlHelper.TAG_SLOT_VALUE:
        if (inSlotCommodityUserSymbol) {
          gncCommodity.setLocalSymbol(characterString);
          inSlotCommodityUserSymbol = false;
        }
        break;

      case GncXmlHelper.TAG_COMMODITY:
        gncCommodities.add(gncCommodity);
        inNodeCommodity = false;
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
  }

  private void sendToAdapter() {

    gnucashFileImportStatus.setCommoditiesSentToAdapter(gncCommodities.size());
    logger.info("{} sent to the adapter", gncCommodities.size());
    commodityAdapter.addRecords(gncCommodities);
    gncCommodities = new ArrayList<>();
  }
}
