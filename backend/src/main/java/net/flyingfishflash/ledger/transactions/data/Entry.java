package net.flyingfishflash.ledger.transactions.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.money.MonetaryAmount;

import com.vladmihalcea.hibernate.type.money.MonetaryAmountType;

import org.hibernate.annotations.CompositeType;

import jakarta.persistence.*;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.NormalBalance;
import net.flyingfishflash.ledger.books.data.Book;

@Entity
public class Entry {

  @Id
  @SequenceGenerator(name = "entry_id_seq", sequenceName = "entry_seq", allocationSize = 1)
  @GeneratedValue(generator = "entry_id_seq")
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Book book;

  @Column(unique = true, updatable = false, length = 32)
  private String guid;

  @ManyToOne(fetch = FetchType.LAZY)
  private Transaction transaction;

  @Enumerated(EnumType.STRING)
  private EntryType type = EntryType.CREDIT;

  private String memo;

  private String action;

  /* Quantity is tied to the account commodity */
  @Column(precision = 38, scale = 18)
  private BigDecimal quantity;

  @Column(precision = 38, scale = 18)
  private BigDecimal quantitySigned;

  @AttributeOverride(
      name = "amount",
      column = @Column(name = "`value`", precision = 38, scale = 18))
  @AttributeOverride(name = "currency", column = @Column(name = "value_currency", length = 3))
  @CompositeType(MonetaryAmountType.class)
  private MonetaryAmount value;

  @Column(precision = 38, scale = 18)
  private BigDecimal valueSigned;

  private Timestamp enterDate; // timestamp without time zone

  @ManyToOne(fetch = FetchType.LAZY)
  private Account account;

  public Entry(Book book) {
    this.book = book;
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

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {

    this.quantity = quantity;
    if (this.account.getNormalBalance() == NormalBalance.CREDIT) {
      if (this.type == EntryType.DEBIT) {
        this.quantitySigned = this.quantity.negate();
      } else {
        this.quantitySigned = this.quantity;
      }
    } else if (this.account.getNormalBalance() == NormalBalance.DEBIT) {
      if (this.type == EntryType.CREDIT) {
        this.quantitySigned = this.quantity.negate();
      } else {
        this.quantitySigned = this.quantity;
      }
    }
  }

  public MonetaryAmount getValue() {
    return value;
  }

  public void setValue(MonetaryAmount value) {
    this.value = value;
    if (this.account.getNormalBalance() == NormalBalance.CREDIT) {
      if (this.type == EntryType.DEBIT) {
        this.valueSigned = this.value.getNumber().numberValue(BigDecimal.class).negate();
      } else {
        this.valueSigned = this.value.getNumber().numberValue(BigDecimal.class);
      }
    } else if (this.account.getNormalBalance() == NormalBalance.DEBIT) {
      if (this.type == EntryType.CREDIT) {
        this.valueSigned = this.value.getNumber().numberValue(BigDecimal.class).negate();
      } else {
        this.valueSigned = this.value.getNumber().numberValue(BigDecimal.class);
      }
    }
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public EntryType getType() {
    return type;
  }

  public void setType(EntryType type) {
    this.type = type;
  }

  public BigDecimal getQuantitySigned() {
    return quantitySigned;
  }

  public Timestamp getEnterDate() {
    return enterDate;
  }

  public void setEnterDate(Timestamp enterDate) {
    this.enterDate = enterDate;
  }

  @Override
  public String toString() {
    return "Entry{"
        + "id="
        + id
        + ", guid='"
        + guid
        + '\''
        + ", transaction="
        + transaction
        + ", ledgerItemType="
        + type
        + ", memo='"
        + memo
        + '\''
        + ", action='"
        + action
        + '\''
        + ", quantity="
        + quantity
        + ", quantitySigned="
        + quantitySigned
        + ", value="
        + value
        + ", valueSigned="
        + valueSigned
        + ", enterDate="
        + enterDate
        + ", account="
        + account
        + '}';
  }
}
