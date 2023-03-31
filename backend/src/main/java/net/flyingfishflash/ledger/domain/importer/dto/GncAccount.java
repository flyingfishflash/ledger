package net.flyingfishflash.ledger.domain.importer.dto;

public class GncAccount {

  private String guid;
  private String name;
  private String fullName;
  private String description;
  private String note;
  private String gncCommodityNamespace;
  private String gncCommodity;
  private String gncAccountType;
  private String accountCode;
  private String parentAccountUID;
  private boolean placeholder;
  private boolean hidden;

  public GncAccount(String name) {
    setName(name);
    this.fullName = this.name;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name.trim();
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getGncCommodityNamespace() {
    return gncCommodityNamespace;
  }

  public void setGncCommodityNamespace(String gncCommodityNamespace) {
    this.gncCommodityNamespace = gncCommodityNamespace;
  }

  public String getGncCommodity() {
    return gncCommodity;
  }

  public void setGncCommodity(String gncCommodity) {
    this.gncCommodity = gncCommodity;
  }

  public String getGncAccountType() {
    return gncAccountType;
  }

  public void setGncAccountType(String gncAccountType) {
    this.gncAccountType = gncAccountType;
  }

  public String getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(String accountCode) {
    this.accountCode = accountCode;
  }

  public String getParentGuid() {
    return parentAccountUID;
  }

  public void setParentGuid(String parentAccountUID) {
    this.parentAccountUID = parentAccountUID;
  }

  public boolean isPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(boolean placeholder) {
    this.placeholder = placeholder;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
}
