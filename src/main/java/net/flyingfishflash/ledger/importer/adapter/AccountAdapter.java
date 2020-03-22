package net.flyingfishflash.ledger.importer.adapter;

import java.util.List;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import net.flyingfishflash.ledger.LedgerConfiguration;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.commodities.service.CommodityService;
import net.flyingfishflash.ledger.importer.dto.GncAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountAdapter {

  private static final Logger logger = LoggerFactory.getLogger(AccountAdapter.class);

  /** Service class for interacting with Accounts */
  private final AccountService accountService;

  /** Service class for interacting with Commodities */
  private final CommodityService commodityService;

  /**
   * Class constructor.
   *
   * <p>Translates GncAccount objects to Account objects and persists the results.
   *
   * @param accountService Service class for interacting with accounts
   * @param commodityService Service class for interacting with commodities
   */
  public AccountAdapter(AccountService accountService, CommodityService commodityService) {

    this.accountService = accountService;
    this.commodityService = commodityService;
  }

  public void addRecords(List<GncAccount> gncAccounts) {

    Account account;
    int persistedCount = 0;

    /*
     * Sort the GncAccount list by account code, nulls sorted to the top.
     * There should only ever be one null, the root account
     *
     * TODO: Evaluate if this sort is necessary, or if the order that GnuCash saves accounts is OK
     *   It seems to be OK, but not confirmed
     */

    /*
      Comparator<GncAccount> gncAccountComparator =
          (comparing(GncAccount::getAccountCode, Comparator.nullsFirst(Comparator.naturalOrder())));
      gncAccounts.sort(gncAccountComparator);
    */

    /* Check that the first account in the list is a Root type account */
    if (!gncAccounts.get(0).getGncAccountType().equalsIgnoreCase("root")) {
      throw new IllegalStateException(
          "The first account in the list of accounts to be imported must be a Root type account.");
    }

    /* Count root accounts */
    long gncRootCount = 0;
    for (GncAccount gncAccount : gncAccounts) {
      if (gncAccount.getGncAccountType().equalsIgnoreCase("root")) {
        gncRootCount++;
      }
    }

    /* Check for multiple root accounts */
    if (gncRootCount > 1) {
      throw new IllegalStateException(
          "The list of accounts to be imported contains more than one Root type account ("
              + gncRootCount
              + "). There must be only one.");
    }

    /* For all accounts without a parent, set their parent account to the Root account */
    for (GncAccount gncAccount : gncAccounts) {
      if (!gncAccount.getGncAccountType().equalsIgnoreCase("root")
          && gncAccount.getParentGuid() == null) {
        gncAccount.setParentGuid(gncAccounts.get(0).getGuid());
      }
    }

    for (GncAccount gncAccount : gncAccounts) {
      /* Create the new account with the imported guid */
      account = new Account(gncAccount.getGuid());
      account.setCode(gncAccount.getAccountCode());
      account.setDescription(gncAccount.getDescription());
      account.setNote(gncAccount.getNote());

      if (gncAccount.getGncAccountType().equalsIgnoreCase("root")) {
        /* If the gncAccountType is Root set some specific values for our imported type */
        account.setType(AccountType.Root);
        /* The root account is the only one that will be hidden */
        account.setHidden(true);
        account.setLongName("root");
        account.setName("root");
        account.setPlaceholder(true);
        accountService.insertAsFirstRoot(account);
        persistedCount++;

      } else {

        /*
        Because we're importing into a nested set structure, the parent accounts must be imported
        before the children.
        */
        account.setParentId(accountService.findByGuid(gncAccount.getParentGuid()).getId());
        account.setName(gncAccount.getName());
        account.setHidden(gncAccount.isHidden());
        account.setPlaceholder(gncAccount.isPlaceholder());
        account.setType(
            AccountType.valueOf(prettyAccountType(gncAccount.getGncAccountType())));
        /* Determine the currency and commodity associated with the account
         * TODO: Commodity and Currency are mutually exclusive. Review this situation.
         */
        if (gncAccount.getGncCommodityNamespace().equalsIgnoreCase("currency")) {
          String currencyMnemonic = Monetary.getCurrency(gncAccount.getGncCommodity()).getCurrencyCode();
          try {
            account.setCurrency(currencyMnemonic);
          } catch (UnknownCurrencyException e) {
            /* TODO: Throw GncImportException with UnknownCurrencyException as the cause or
             *  potentially set the currency to 'XXX' */
            logger.info(e.getMessage());
            throw new UnknownCurrencyException(currencyMnemonic);
          }
        } else {
          account.setCommodity(
              commodityService.findByNameSpaceAndMnemonic(
                  gncAccount.getGncCommodityNamespace(), gncAccount.getGncCommodity()));
          /* Set the currency to the default currency */
          account.setCurrency(LedgerConfiguration.defaultCurrency.getCurrencyCode());
        }
        accountService.insertAsLastChildOf(
            account, accountService.findByGuid(gncAccount.getParentGuid()));
        persistedCount++;
      } // if gncAccountType == ROOT
    }
    logger.info(persistedCount + " persisted");
  }

  /** Capitalize the first letter of the account type string */
  private String prettyAccountType(String accountType) {

    return accountType.substring(0, 1).toUpperCase() + accountType.substring(1).toLowerCase();
  }
}
