package net.flyingfishflash.ledger.importer;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.flyingfishflash.ledger.importer.adapter.TransactionAdapter;
import net.flyingfishflash.ledger.importer.dto.GncSplit;
import net.flyingfishflash.ledger.importer.dto.GncTransaction;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for parsing Transaction and LedgerItem elements found in the GnuCash XML file. The
 * discovered elements are passed to an adapter class to be translated and persisting in the
 * database.
 */
@Component
public class GncXmlTransactionHandler extends DefaultHandler {

  private static final Logger logger = LoggerFactory.getLogger(GncXmlTransactionHandler.class);

  /** ISO 4217 currency code for "No Currency" */
  private static final String NO_CURRENCY_CODE = "XXX";

  private TransactionAdapter transactionAdapter;

  private GnucashFileImportStatus gnucashFileImportStatus;

  /** StringBuilder for accumulating characters between XML tags */
  StringBuilder content = new StringBuilder();

  GncTransaction gncTransaction;

  GncSplit gncSplit;

  List<GncTransaction> gncTransactions;
  List<GncTransaction> templateTransactions = new ArrayList<>();
  Map<String, String> templateAccountToTransactionMap = new HashMap<>();

  boolean inNodeCountDataTransaction = false;
  boolean inNodeTransaction = false;
  boolean inNodeTransactionCurrency = false;
  boolean inNodeTransactionDatePosted = false;
  boolean inNodeTransactionDateEntered = false;
  boolean inNodeTemplateTransactions = false;
  boolean ignoreTemplateTransaction = true;

  /** Transaction count as indicated in <gnc:count-data cd:type=transaction"><</gnc:count-data> */
  int nodeCountDataTransactionCount = -1;

  /** Saves the attribute of the slot tag Used for determining where we are in the budget amounts */
  String slotTagAttribute = null;


  /**
   * Creates a handler for handling XML stream events when parsing the XML backup file
   *
   * @param transactionAdapter
   * @param gnucashFileImportStatus
   */
  public GncXmlTransactionHandler(TransactionAdapter transactionAdapter,
      GnucashFileImportStatus gnucashFileImportStatus) {
    this.transactionAdapter = transactionAdapter;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @Override
  public void startElement(
      String uri, String localName, String qualifiedName, Attributes attributes)
      throws SAXException {

    switch (qualifiedName) {
      case GncXmlHelper.TAG_ACCOUNT:
        inNodeTransaction = true;
        break;

      case GncXmlHelper.TAG_TRANSACTION:
        gncTransaction = new GncTransaction("");
        break;

      case GncXmlHelper.TAG_TRX_CURRENCY:
        inNodeTransactionCurrency = true;

      case GncXmlHelper.TAG_TRN_SPLIT:
        gncSplit = new GncSplit("");
        break;

      case GncXmlHelper.TAG_DATE_POSTED:
        inNodeTransactionDatePosted = true;
        break;

      case GncXmlHelper.TAG_DATE_ENTERED:
        inNodeTransactionDateEntered = true;
        break;

      case GncXmlHelper.TAG_TEMPLATE_TRANSACTIONS:
        inNodeTemplateTransactions = true;
        break;

      case GncXmlHelper.TAG_SLOT:
        break;

      case GncXmlHelper.TAG_SLOT_VALUE:
        slotTagAttribute = attributes.getValue(GncXmlHelper.ATTR_KEY_TYPE);
        break;

      case GncXmlHelper.TAG_COUNT_DATA:
        if (attributes.getValue("cd:type").equals("transaction")) {
          inNodeCountDataTransaction = true;
        }
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qualifiedName) throws SAXException {

    String characterString = content.toString().trim();

    if (content.length() == 0) {
      characterString = null;
    }

    switch (qualifiedName) {
      case GncXmlHelper.TAG_COUNT_DATA:
        if (inNodeCountDataTransaction) {
          nodeCountDataTransactionCount = Integer.parseInt(characterString);
          if (nodeCountDataTransactionCount > 0) {
            gncTransactions = new ArrayList<>(nodeCountDataTransactionCount);
          }
        }
        inNodeCountDataTransaction = false;
        break;

      case GncXmlHelper.TAG_TRX_ID:
        gncTransaction.setGuid(characterString);
        break;

      case GncXmlHelper.TAG_COMMODITY_SPACE:
        /* Consider using the commodity namespace field */
        break;

      case GncXmlHelper.TAG_COMMODITY_ID:
        if (inNodeTransactionCurrency) gncTransaction.setCurrency(characterString);
        break;

      case GncXmlHelper.TAG_TRX_CURRENCY:
        inNodeTransactionCurrency = false;
        break;

      case GncXmlHelper.TAG_TRN_NUM:
        gncTransaction.setNum(characterString);
        break;

      case GncXmlHelper.TAG_TRN_DESCRIPTION:
        gncTransaction.setDescription(characterString);
        break;

      case GncXmlHelper.TAG_TS_DATE:
        /* These dates are zoned UTC in the GnuCash XML */
        try {
          if (inNodeTransactionDatePosted && gncTransaction != null) {
            // gncTransaction.setTime(GncXmlHelper.parseDate(characterString));
            gncTransaction.setDatePosted(LocalDate.parse(characterString.substring(0, 10)));
            inNodeTransactionDatePosted = false;
          }
          if (inNodeTransactionDateEntered && gncTransaction != null) {
            Timestamp timestamp = new Timestamp(GncXmlHelper.parseDate(characterString));
            // gncTransaction.setCreatedTimestamp(timestamp);
            inNodeTransactionDateEntered = false;
          }
        } catch (ParseException e) {
          String message = "Unable to parse transaction time - " + characterString;
          throw new SAXException(message, e);
        }
        break;

      case GncXmlHelper.TAG_SPLIT_ID:
        gncSplit.setGuid(characterString);
        break;

      case GncXmlHelper.TAG_SPLIT_MEMO:
        gncSplit.setMemo(characterString);
        break;

      case GncXmlHelper.TAG_SPLIT_VALUE:
        gncSplit.setValue(characterString);
        break;

      case GncXmlHelper.TAG_SPLIT_QUANTITY:
        gncSplit.setQuantity(characterString);
        break;

      case GncXmlHelper.TAG_SPLIT_ACCOUNT:
        if (!inNodeTemplateTransactions) {
          gncSplit.setAccountGuid(characterString);
        } else {
          if (!ignoreTemplateTransaction)
            templateAccountToTransactionMap.put(characterString, gncTransaction.getGuid());
        }
        break;

      case GncXmlHelper.TAG_TRN_SPLIT:
        gncTransaction.addSplit(gncSplit);
        break;

      case GncXmlHelper.TAG_TRANSACTION:
        gncTransaction.setTemplate(inNodeTemplateTransactions);
        if (inNodeTemplateTransactions) {
          if (!ignoreTemplateTransaction) templateTransactions.add(gncTransaction);
        } else {
          gncTransactions.add(gncTransaction);
        }
        ignoreTemplateTransaction = true;
        gncTransaction = null;
        break;

      case GncXmlHelper.TAG_TEMPLATE_TRANSACTIONS:
        inNodeTemplateTransactions = false;
        break;
    }

    content.setLength(0);
  }

  @Override
  public void characters(char[] chars, int start, int length) throws SAXException {
    content.append(chars, start, length);
  }

  @Override
  public void endDocument() throws SAXException {

    super.endDocument();
    sendToAdapter();
  }

  private void sendToAdapter() {

    logger.info(
        nodeCountDataTransactionCount + " indicated by gnc:count-data cd:type=\"transaction\"");
    logger.info(templateTransactions.size() + " template transactions");
    logger.info(gncTransactions.size() + " sent to the adapter");
    gnucashFileImportStatus.setTransactionsGncCount(nodeCountDataTransactionCount);
    gnucashFileImportStatus.setTransactionsTemplates(templateTransactions.size());
    gnucashFileImportStatus.setTransactionsSentToAdapter(gncTransactions.size());
    transactionAdapter.addRecords(gncTransactions);
    gncTransactions = null;
  }
}
