package net.flyingfishflash.ledger.accounts.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;

public class AccountDto {

  private AccountCategory accountCategory;

  private AccountType accountType;

  @Size(max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  private String code;

  @Size(max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  private String description;

  @Size(max = 2048)
  @NotBlank
  private String discriminator;

  @Size(max = 2048)
  @NotBlank
  private String guid;

  @NotNull public Boolean hidden;

  @Positive @NotNull public Long id;

  @Size(max = 2048)
  @NotBlank
  private String longName;

  @Size(max = 2048)
  @NotBlank
  private String name;

  @Size(max = 4096)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  private String note;

  @Positive @NotNull private Long parentId;

  @NotNull private Boolean placeholder;

  @NotNull private Boolean taxRelated;

  @Positive private Long treeLeft;

  @Positive private Long treeLevel;

  @Positive private Long treeRight;

  public AccountDto(Account account) {

    this.id = account.getId();
    this.treeLeft = account.getTreeLeft();
    this.treeRight = account.getTreeRight();
    this.treeLevel = account.getTreeLevel();
    this.parentId = account.getParentId();
    this.discriminator = account.getDiscriminator();
    this.guid = account.getGuid();
    this.name = account.getName();
    this.longName = account.getLongName();
    this.code = account.getCode();
    this.description = account.getDescription();
    this.note = account.getNote();
    this.placeholder = account.getPlaceholder();
    this.hidden = account.getHidden();
    this.taxRelated = account.getTaxRelated();
    this.accountCategory = account.getCategory();
    this.accountType = account.getType();
  }

  public AccountCategory getAccountCategory() {
    return accountCategory;
  }

  public void setAccountCategory(AccountCategory accountCategory) {
    this.accountCategory = accountCategory;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDiscriminator() {
    return discriminator;
  }

  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Boolean getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(Boolean placeholder) {
    this.placeholder = placeholder;
  }

  public Boolean getTaxRelated() {
    return taxRelated;
  }

  public void setTaxRelated(Boolean taxRelated) {
    this.taxRelated = taxRelated;
  }

  public Long getTreeLeft() {
    return treeLeft;
  }

  public void setTreeLeft(Long treeLeft) {
    this.treeLeft = treeLeft;
  }

  public Long getTreeLevel() {
    return treeLevel;
  }

  public void setTreeLevel(Long treeLevel) {
    this.treeLevel = treeLevel;
  }

  public Long getTreeRight() {
    return treeRight;
  }

  public void setTreeRight(Long treeRight) {
    this.treeRight = treeRight;
  }

  @Override
  public String toString() {
    return "AccountDto{"
        + "accountCategory="
        + accountCategory
        + ", accountType="
        + accountType
        + ", code='"
        + code
        + '\''
        + ", description='"
        + description
        + '\''
        + ", discriminator='"
        + discriminator
        + '\''
        + ", guid='"
        + guid
        + '\''
        + ", hidden="
        + hidden
        + ", id="
        + id
        + ", longName='"
        + longName
        + '\''
        + ", name='"
        + name
        + '\''
        + ", note='"
        + note
        + '\''
        + ", parentId="
        + parentId
        + ", placeholder="
        + placeholder
        + ", taxRelated="
        + taxRelated
        + ", treeLeft="
        + treeLeft
        + ", treeLevel="
        + treeLevel
        + ", treeRight="
        + treeRight
        + '}';
  }
}
