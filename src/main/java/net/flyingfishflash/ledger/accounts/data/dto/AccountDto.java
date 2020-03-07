package net.flyingfishflash.ledger.accounts.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.data.Account;

public class AccountDto {

  public AccountCategory accountCategory;

  public AccountType accountType;

  @Size(max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String code;

  @Size(max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String description;

  @Size(max = 2048)
  @NotBlank
  public String discriminator;

  @Size(max = 2048)
  @NotBlank
  public String guid;

  @NotNull public Boolean hidden;

  @Positive @NotNull public Long id;

  @Size(max = 2048)
  @NotBlank
  public String longName;

  @Size(max = 2048)
  @NotBlank
  public String name;

  @Size(max = 4096)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String note;

  @Positive @NotNull public Long parentId;

  @NotNull public Boolean placeholder;

  @NotNull public Boolean taxRelated;

  @Positive public Long treeLeft;

  @Positive public Long treeLevel;

  @Positive public Long treeRight;

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

  @Override
  public String toString() {
    return "AccountDto{" +
        "accountCategory=" + accountCategory +
        ", accountType=" + accountType +
        ", code='" + code + '\'' +
        ", description='" + description + '\'' +
        ", discriminator='" + discriminator + '\'' +
        ", guid='" + guid + '\'' +
        ", hidden=" + hidden +
        ", id=" + id +
        ", longName='" + longName + '\'' +
        ", name='" + name + '\'' +
        ", note='" + note + '\'' +
        ", parentId=" + parentId +
        ", placeholder=" + placeholder +
        ", taxRelated=" + taxRelated +
        ", treeLeft=" + treeLeft +
        ", treeLevel=" + treeLevel +
        ", treeRight=" + treeRight +
        '}';
  }
}
