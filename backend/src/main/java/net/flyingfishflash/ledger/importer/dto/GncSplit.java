package net.flyingfishflash.ledger.importer.dto;

public class GncSplit /*extends GncBaseModel*/ {

  private String guid;
  private String accountGuid;
  private String transactionGuid;
  private String quantity;
  private String value;
  private String memo;

  public GncSplit(String accountUID) {
    setAccountGuid(accountUID);
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getAccountGuid() {
    return accountGuid;
  }

  public void setAccountGuid(String accountGuid) {
    this.accountGuid = accountGuid;
  }

  public String getTransactionGuid() {
    return transactionGuid;
  }

  public void setTransactionGuid(String transactionGuid) {
    this.transactionGuid = transactionGuid;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  @Override
  public String toString() {
    return "GncSplitSparse{"
        + "guid='"
        + guid
        + '\''
        + ", accountGuid='"
        + accountGuid
        + '\''
        + ", transactionGuid='"
        + transactionGuid
        + '\''
        + ", quantity='"
        + quantity
        + '\''
        + ", value='"
        + value
        + '\''
        + ", memo='"
        + memo
        + '\''
        + '}';
  }
}
