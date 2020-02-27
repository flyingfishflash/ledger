package net.flyingfishflash.ledger.commodities.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "commodity",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"namespace", "mnemonic"})})
public class Commodity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "guid", unique = true, updatable = false, length = 32)
  private String guid;

  @Column(name = "namespace")
  private String namespace;

  @Column(name = "mnemonic")
  private String mnemonic;

  @Column(name = "fullname")
  private String fullName;

  @Column(name = "fraction")
  private Integer fraction;

  @Column(name = "custom_identifier")
  private String customIdentifier;

  @Column(name = "cusip", length = 9)
  private String cusip;

  @Column(name = "quote_remote")
  private boolean quoteRemote;

  @Column(name = "quote_remote_source")
  private String quoteRemoteSource;

  public Commodity() {}

  public Commodity(String guid) {
    this.guid = guid;
  }

  public String getGuid() {
    return guid;
  }

  private void setGuid(String guid) {
    this.guid = guid;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getMnemonic() {
    return mnemonic;
  }

  public void setMnemonic(String mnemonic) {
    this.mnemonic = mnemonic;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Integer getFraction() {
    return fraction;
  }

  public void setFraction(Integer fraction) {
    this.fraction = fraction;
  }

  public String getCustomIdentifier() {
    return customIdentifier;
  }

  public void setCustomIdentifier(String customIdentifier) {
    this.customIdentifier = customIdentifier;
  }

  public String getCusip() {
    return cusip;
  }

  public void setCusip(String cusip) {
    this.cusip = cusip;
  }

  public boolean isQuoteRemote() {
    return quoteRemote;
  }

  public void setQuoteRemote(boolean quoteRemote) {
    this.quoteRemote = quoteRemote;
  }

  public String getQuoteRemoteSource() {
    return quoteRemoteSource;
  }

  public void setQuoteRemoteSource(String quoteRemoteSource) {
    this.quoteRemoteSource = quoteRemoteSource;
  }

  @Override
  public String toString() {
    return "Commodity{"
        + "id="
        + id
        + ", guid='"
        + guid
        + '\''
        + ", namespace='"
        + namespace
        + '\''
        + ", mnemonic='"
        + mnemonic
        + '\''
        + ", fullName='"
        + fullName
        + '\''
        + ", fraction="
        + fraction
        + ", cusip='"
        + cusip
        + '\''
        + ", quoteRemote="
        + quoteRemote
        + ", quoteRemoteSource='"
        + quoteRemoteSource
        + '\''
        + '}';
  }
}
