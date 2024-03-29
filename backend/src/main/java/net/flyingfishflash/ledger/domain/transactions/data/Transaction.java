package net.flyingfishflash.ledger.domain.transactions.data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

import jakarta.persistence.*;

import net.flyingfishflash.ledger.domain.books.data.Book;

@Entity
public class Transaction {

  @Id
  @SequenceGenerator(
      name = "transaction_id_seq",
      sequenceName = "transaction_seq",
      allocationSize = 1)
  @GeneratedValue(generator = "transaction_id_seq")
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Book book;

  @Column(unique = true, updatable = false, length = 32)
  private String guid;

  private String currency;

  private String num;

  private LocalDate postDate;

  private Timestamp enterDate;

  private String description;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Entry> entries = new ArrayList<>();

  public Transaction(Book book) {
    this.book = book;
  }

  public void addEntry(Entry entry) {
    entries.add(entry);
    entry.setTransaction(this);
  }

  public void removeEntry(Entry entry) {
    entries.remove(entry);
    entry.setTransaction(null);
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public LocalDate getPostDate() {
    return postDate;
  }

  public void setPostDate(LocalDate postDate) {
    this.postDate = postDate;
  }

  public Timestamp getEnterDate() {
    return enterDate;
  }

  public void setEnterDate(Timestamp enterDate) {
    this.enterDate = enterDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  private MonetaryAmount getImbalance() {

    var imbalance = Money.of(0, Monetary.getCurrency(this.getCurrency()));

    for (Entry entry : entries) {
      MonetaryAmount amount = entry.getValue();
      if (entry.getType() == EntryType.DEBIT) {
        imbalance = imbalance.subtract(amount);
      } else {
        imbalance = imbalance.add(amount);
      }
    }
    return imbalance;
  }

  public Entry createAutoBalanceEntry() {

    MonetaryAmount imbalance =
        getImbalance(); // returns imbalance of 0 for multicurrency transactions
    if (!imbalance.isZero()) {
      var entry = new Entry(this.book);
      entry.setType(imbalance.isNegative() ? EntryType.CREDIT : EntryType.DEBIT);
      // addLedgerItem(entry);
      return entry;
    }
    return null;
  }

  @Override
  public String toString() {
    return "Transaction{"
        + "id="
        + id
        + ", guid='"
        + guid
        + '\''
        + ", currency="
        + currency
        + ", num='"
        + num
        + '\''
        + ", postDate="
        + postDate
        + ", enterDate="
        + enterDate
        + ", description='"
        + description
        + '\''
        + ", entries="
        + entries
        + '}';
  }
}
