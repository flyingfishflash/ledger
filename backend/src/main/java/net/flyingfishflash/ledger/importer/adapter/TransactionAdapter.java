package net.flyingfishflash.ledger.importer.adapter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.money.Monetary;
import javax.money.UnknownCurrencyException;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.flyingfishflash.ledger.accounts.service.AccountService;
import net.flyingfishflash.ledger.importer.GncXmlHelper;
import net.flyingfishflash.ledger.importer.dto.GncSplit;
import net.flyingfishflash.ledger.importer.dto.GncTransaction;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.transactions.data.Entry;
import net.flyingfishflash.ledger.transactions.data.EntryType;
import net.flyingfishflash.ledger.transactions.data.Transaction;
import net.flyingfishflash.ledger.transactions.service.TransactionService;

@Component
public class TransactionAdapter {

  private static final Logger logger = LoggerFactory.getLogger(TransactionAdapter.class);

  private final TransactionService transactionService;
  private final AccountService accountService;
  private GnucashFileImportStatus gnucashFileImportStatus;

  public TransactionAdapter(
      TransactionService transactionService,
      AccountService accountService,
      GnucashFileImportStatus gnucashFileImportStatus) {
    this.transactionService = transactionService;
    this.accountService = accountService;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  @SuppressWarnings("java:S3776")
  public void addRecords(List<GncTransaction> gncTransactions) {

    List<Transaction> transactions = new ArrayList<>(gncTransactions.size());

    for (GncTransaction gncTransaction : gncTransactions) {

      var transaction = new Transaction();
      transaction.setGuid(gncTransaction.getGuid());
      transaction.setDescription(gncTransaction.getDescription());
      transaction.setNum(gncTransaction.getNum());
      transaction.setPostDate(gncTransaction.getDatePosted());

      /* Set the transaction currency. Only ISO 4217 currencies are permitted */
      try {
        var currency = Monetary.getCurrency(gncTransaction.getCurrency()).toString();
        transaction.setCurrency(currency);
      } catch (UnknownCurrencyException e) {
        /* TODO: Throw GncImportException with UnknownCurrencyException as the cause */
        logger.info(e.getMessage());
        throw new UnknownCurrencyException(gncTransaction.getCurrency());
      }

      List<GncSplit> gncTransactionSplits;
      gncTransactionSplits = gncTransaction.getSplits();

      for (GncSplit gncSplit : gncTransactionSplits) {
        var entry = new Entry();
        entry.setGuid(gncSplit.getGuid());
        entry.setMemo(gncSplit.getMemo());

        /* Account must be set prior to quantity */
        if (!gncSplit.getAccountGuid().equals("zzz")) {
          entry.setAccount(accountService.findByGuid(gncSplit.getAccountGuid()));
        }

        /* Determine if the entry is a credit or debit (in the context of Gnucash)
         * Gnucash stores debits signed '-' */
        if (gncSplit.getValue().charAt(0) == '-') {
          entry.setType(EntryType.CREDIT);
        } else {
          entry.setType(EntryType.DEBIT);
        }

        BigDecimal quantity;
        BigDecimal value;

        try {
          quantity = GncXmlHelper.parseSplitAmount(gncSplit.getQuantity()).abs();
        } catch (ParseException e) {
          throw new IllegalStateException("error parsing: " + gncSplit.getQuantity(), e);
        }
        try {
          value = GncXmlHelper.parseSplitAmount(gncSplit.getValue()).abs();
        } catch (ParseException e) {
          throw new IllegalStateException("error parsing: " + gncSplit.getValue(), e);
        }

        /* Tied to the entry's account commodity */
        entry.setQuantity(quantity);

        /* Tied to the transaction currency */
        var money = Money.of(value, transaction.getCurrency());
        entry.setValue(money);

        transaction.addEntry(entry);
      }

      transactions.add(transaction);

      /* Check if transaction is imbalanced */
      /* TODO: Handle imbalanced split, assign to Imbalance account */
      var imbalancedEntry = transaction.createAutoBalanceEntry();
      if (imbalancedEntry != null) {
        throw new IllegalStateException("Unbalanced Entry: " + imbalancedEntry.toString());
      }
    }

    transactionService.saveAllTransactions(transactions);
    logger.info("{} persisted", transactions.size());
    gnucashFileImportStatus.setTransactionsPersisted(transactions.size());
  }
}
