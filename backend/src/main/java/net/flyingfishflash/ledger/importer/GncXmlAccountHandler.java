package net.flyingfishflash.ledger.importer;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.flyingfishflash.ledger.importer.adapter.AccountAdapter;
import net.flyingfishflash.ledger.importer.dto.GncAccount;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;

/**
 * Handler for parsing Account elements found in the GnuCash XML file. The discovered accounts are
 * passed to an adapter class to be translated and persisted in the database.
 */
@Component
public class GncXmlAccountHandler extends DefaultHandler {

  private static final Logger logger = LoggerFactory.getLogger(GncXmlAccountHandler.class);

  /** Translates GncAccount objects into Account objects and persists the results */
  private final AccountAdapter accountAdapter;

  private GnucashFileImportStatus gnucashFileImportStatus;

  /** Accumulates characters between XML tags */
  StringBuilder content = new StringBuilder();

  /** Discovered account */
  GncAccount gncAccount;

  /** Accumulates discovered accounts, passed to the adapter for translation and persistence */
  ArrayList<GncAccount> gncAccounts;

  boolean inNodeCountDataAccount = false;
  boolean inNodeAccount = false;
  boolean inNodeTemplateTransactions = false;
  boolean inNodeSlot = false;
  boolean inPlaceHolderSlot = false;

  /** Count of accounts as indicated in <gnc:count-data cd:type=account"><</gnc:count-data> * */
  int nodeCountDataAccountCount = -1;

  /** Saves the attribute of the slot tag Used for determining where we are in the budget amounts */
  String slotTagAttribute = null;

  /** Creates a handler for handling XML stream events when parsing the XML backup file */
  public GncXmlAccountHandler(
      AccountAdapter accountAdapter, GnucashFileImportStatus gnucashFileImportStatus) {

    this.accountAdapter = accountAdapter;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @Override
  public void startElement(
      String uri, String localName, String qualifiedName, Attributes attributes)
      throws SAXException {

    switch (qualifiedName) {
      case GncXmlHelper.TAG_ACCOUNT:
        gncAccount = new GncAccount("");
        inNodeAccount = true;
        break;

      case GncXmlHelper.TAG_SLOT_VALUE:
        slotTagAttribute = attributes.getValue(GncXmlHelper.ATTR_KEY_TYPE);
        break;

      case GncXmlHelper.TAG_TEMPLATE_TRANSACTIONS:
        inNodeTemplateTransactions = true;
        break;

      case GncXmlHelper.TAG_COUNT_DATA:
        if (attributes.getValue("cd:type").equals("account")) {
          inNodeCountDataAccount = true;
        }
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qualifiedName) throws SAXException {

    String elementData = content.toString().trim();

    if (content.length() == 0) {
      elementData = null;
    }

    switch (qualifiedName) {
      case GncXmlHelper.TAG_COUNT_DATA:
        if (inNodeCountDataAccount) {
          nodeCountDataAccountCount = Integer.parseInt(elementData);
          if (nodeCountDataAccountCount > 0) {
            gncAccounts = new ArrayList<>(nodeCountDataAccountCount);
          }
        }
        inNodeCountDataAccount = false;
        break;

      case GncXmlHelper.TAG_ACCT_NAME:
        gncAccount.setName(elementData);
        gncAccount.setFullName(elementData);
        break;

      case GncXmlHelper.TAG_ACCT_ID:
        gncAccount.setGuid(elementData);
        break;

      case GncXmlHelper.TAG_ACCT_TYPE:
        gncAccount.setGncAccountType(elementData);
        break;

      case GncXmlHelper.TAG_ACCT_CODE:
        gncAccount.setAccountCode(elementData);
        break;

      case GncXmlHelper.TAG_COMMODITY_SPACE:
        if (inNodeAccount) {
          gncAccount.setGncCommodityNamespace(elementData);
        }
        break;

      case GncXmlHelper.TAG_COMMODITY_ID:
        if (inNodeAccount) {
          gncAccount.setGncCommodity(elementData);
        }
        break;

      case GncXmlHelper.TAG_ACCT_DESCRIPTION:
        gncAccount.setDescription(elementData);
        break;

      case GncXmlHelper.TAG_PARENT_UID:
        gncAccount.setParentGuid(elementData);
        break;

      case GncXmlHelper.TAG_SLOT_KEY:
        switch (elementData) {
          case GncXmlHelper.KEY_PLACEHOLDER:
            inPlaceHolderSlot = true;
            break;

          case GncXmlHelper.KEY_NOTES:
            inNodeSlot = true;
            break;
        }
        break;

      case GncXmlHelper.TAG_SLOT_VALUE:
        if (inPlaceHolderSlot) {
          gncAccount.setPlaceholder(Boolean.parseBoolean(elementData));
          inPlaceHolderSlot = false;
        } else if (inNodeSlot && inNodeAccount) {
          gncAccount.setNote(elementData);
          inNodeSlot = false;
        }
        break;

      case GncXmlHelper.TAG_ACCOUNT:
        if (!inNodeTemplateTransactions) {
          gncAccounts.add(gncAccount);
        }
        gncAccount = null;
        inNodeAccount = false;
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

    logger.info(nodeCountDataAccountCount + " indicated by gnc:count-data cd:type=\"account\"");
    logger.info(gncAccounts.size() + " sent to the adapter");
    gnucashFileImportStatus.setAccountsGncCount(gncAccounts.size());
    gnucashFileImportStatus.setAccountsSentToAdapter(nodeCountDataAccountCount);

    /*
     * Sort the GncAccount list by account code, nulls sorted to the top.
     * There should only ever be one null, the root account
     *
     * The order that GnuCash saves accounts by default is OK.
     *
     * TODO: Make sorting by account code on import optional
     */

    Comparator<GncAccount> gncAccountComparator =
        (comparing(GncAccount::getAccountCode, Comparator.nullsFirst(Comparator.naturalOrder())));
    gncAccounts.sort(gncAccountComparator);

    accountAdapter.addRecords(gncAccounts);
    gncAccounts = null;
  }
}
