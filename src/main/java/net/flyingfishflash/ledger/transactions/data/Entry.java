package net.flyingfishflash.ledger.transactions.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.javamoney.moneta.Money;

import net.flyingfishflash.ledger.accounts.data.Account;

@Entity
@TypeDef(
    name = "persistentMoneyAmountAndCurrency",
    typeClass = PersistentMoneyAmountAndCurrency.class)
public class Entry {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

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

  /* Value is tied to the transaction currency */
  @Columns(
      columns = {
        @Column(name = "value_currency", length = 3),
        @Column(name = "value", precision = 38, scale = 18)
      })
  @Type(type = "persistentMoneyAmountAndCurrency")
  private Money value;

  @Column(precision = 38, scale = 18)
  private BigDecimal valueSigned;

  private Timestamp enterDate; // timestamp without time zone

  @ManyToOne(fetch = FetchType.LAZY)
  private Account account;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
    switch (this.account.getNormalBalance()) {
      case DEBIT:
        if (this.type == EntryType.CREDIT) {
          this.quantitySigned = this.quantity.negate();
        } else this.quantitySigned = this.quantity;
        break;
      case CREDIT:
        if (this.type == EntryType.DEBIT) {
          this.quantitySigned = this.quantity.negate();
        } else this.quantitySigned = this.quantity;
        break;
    }
  }

  public Money getValue() {
    return value;
  }

  public void setValue(Money value) {
    this.value = value;
    switch (this.account.getNormalBalance()) {
      case DEBIT:
        if (this.type == EntryType.CREDIT) {
          this.valueSigned = this.value.negate().getNumberStripped();
        } else this.valueSigned = this.value.getNumberStripped();
        break;
      case CREDIT:
        if (this.type == EntryType.DEBIT) {
          this.valueSigned = this.value.negate().getNumberStripped();
        } else this.valueSigned = this.value.getNumberStripped();
        break;
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

  public Timestamp getEnter_date() {
    return enterDate;
  }

  public void setEnter_date(Timestamp enter_date) {
    this.enterDate = enter_date;
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
        + ", enter_date="
        + enterDate
        + ", account="
        + account
        + '}';
  }
}
