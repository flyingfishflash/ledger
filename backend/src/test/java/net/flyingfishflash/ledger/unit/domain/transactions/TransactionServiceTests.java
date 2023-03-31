package net.flyingfishflash.ledger.unit.domain.transactions;

import static net.flyingfishflash.ledger.ApplicationConfiguration.DEFAULT_CURRENCY;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.domain.accounts.data.Account;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.transactions.data.Entry;
import net.flyingfishflash.ledger.domain.transactions.data.EntryType;
import net.flyingfishflash.ledger.domain.transactions.data.Transaction;

// TODO: Organize and just overall fix these tests
@ExtendWith(MockitoExtension.class)
class TransactionServiceTests {

  @Test
  void transactionGetBook() {
    var bookName = "Lorem Ipsum";
    var book = new Book(bookName);
    var transaction = new Transaction(book);
    assertThat(transaction.getBook()).isEqualTo(book);
  }

  @Test
  void entrySetValueAssetAccount() {
    var bookName = "Lorem Ipsum";
    var book = new Book(bookName);
    var entry = new Entry(book);
    Money moneyof = Money.of(12, DEFAULT_CURRENCY);
    var account = new Account();
    account.setBook(book);
    account.setType(AccountType.ASSET);
    entry.setAccount(account);
    entry.setValue(moneyof);
    entry.setQuantity(moneyof.getNumber().numberValue(BigDecimal.class));
    assertThat(entry.getQuantitySigned()).isLessThan(BigDecimal.ZERO);
    System.out.println(entry);
    entry.setType(EntryType.DEBIT);
    entry.setValue(moneyof);
    entry.setQuantity(moneyof.getNumber().numberValue(BigDecimal.class));
    System.out.println(entry);
    assertThat(entry.getQuantitySigned()).isGreaterThan((BigDecimal.ZERO));
    assertThat(moneyof).isEqualTo(entry.getValue());
  }

  @Test
  void entrySetValueLiabilityAccount() {
    var bookName = "Lorem Ipsum";
    var book = new Book(bookName);
    var entry = new Entry(book);
    Money moneyof = Money.of(12, DEFAULT_CURRENCY);
    var account = new Account();
    account.setBook(book);
    account.setType(AccountType.LIABILITY);
    entry.setAccount(account);
    entry.setType(EntryType.DEBIT);
    entry.setValue(moneyof);
    entry.setQuantity(moneyof.getNumber().numberValue(BigDecimal.class));
    assertThat(entry.getQuantitySigned()).isLessThan(BigDecimal.ZERO);
    entry.setType(EntryType.CREDIT);
    entry.setValue(moneyof);
    entry.setQuantity(moneyof.getNumber().numberValue(BigDecimal.class));
    assertThat(entry.getQuantitySigned()).isGreaterThan((BigDecimal.ZERO));
    assertThat(moneyof).isEqualTo(entry.getValue());
  }

  @Test
  void entryCreateAutoBalanceEntry() {
    var bookName = "Lorem Ipsum";
    var book = new Book(bookName);
    var transaction = new Transaction(book);
    transaction.setCurrency(DEFAULT_CURRENCY.getCurrencyCode());
    var entry1 = new Entry(book);
    Money entryOneMoney = Money.of(12, DEFAULT_CURRENCY);
    var entryOneAccount = new Account();
    entryOneAccount.setBook(book);
    entryOneAccount.setType(AccountType.LIABILITY);
    entry1.setAccount(entryOneAccount);
    entry1.setType(EntryType.DEBIT);
    entry1.setValue(entryOneMoney);
    entry1.setQuantity(entryOneMoney.getNumber().numberValue(BigDecimal.class));
    var entry2 = new Entry(book);
    Money entryTwoMoney = Money.of(12, DEFAULT_CURRENCY);
    var entryTwoAccount = new Account();
    entryTwoAccount.setBook(book);
    entryTwoAccount.setType(AccountType.ASSET);
    entry2.setAccount(entryTwoAccount);
    entry2.setType(EntryType.CREDIT);
    entry2.setValue(entryTwoMoney);
    entry2.setQuantity(entryTwoMoney.getNumber().numberValue(BigDecimal.class));
    transaction.addEntry(entry1);
    transaction.addEntry(entry2);
    var imbalanceEntry = transaction.createAutoBalanceEntry();
    assertThat(imbalanceEntry).isNull();
  }
}
