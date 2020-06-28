package net.flyingfishflash.ledger.importer.dto;

public class GncCommodity {

  private String namespace;
  private String mnemonic;
  private String fullName;
  private String cusip;
  private String localSymbol;
  private int smallestFraction = 100;
  private boolean quoteRemote = false;

  public GncCommodity() {}

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getMnemonic() {
    return mnemonic;
  }

  public void setMnemonic(String mMnemonic) {
    this.mnemonic = mMnemonic;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String mFullname) {
    this.fullName = mFullname;
  }

  public String getCusip() {
    return cusip;
  }

  public void setCusip(String mCusip) {
    this.cusip = mCusip;
  }

  public String getLocalSymbol() {
    return localSymbol;
  }

  public void setLocalSymbol(String localSymbol) {
    this.localSymbol = localSymbol;
  }

  public int getSmallestFraction() {
    return smallestFraction;
  }

  public void setSmallestFraction(int smallestFraction) {
    this.smallestFraction = smallestFraction;
  }

  public boolean getQuoteRemote() {
    return quoteRemote;
  }

  public void setQuoteRemote(boolean quoteRemote) {
    this.quoteRemote = quoteRemote;
  }

  @Override
  public String toString() {
    return "GncCommodity{"
        + "namespace='"
        + namespace
        + '\''
        + ", mnemonic='"
        + mnemonic
        + '\''
        + ", fullName='"
        + fullName
        + '\''
        + ", cusip='"
        + cusip
        + '\''
        + ", localSymbol='"
        + localSymbol
        + '\''
        + ", smallestFraction="
        + smallestFraction
        + ", quoteRemote="
        + quoteRemote
        + '}';
  }
}
