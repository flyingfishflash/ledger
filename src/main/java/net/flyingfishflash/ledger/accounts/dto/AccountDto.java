package net.flyingfishflash.ledger.accounts.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import net.flyingfishflash.ledger.accounts.Account;
import net.flyingfishflash.ledger.accounts.AccountCategory;
import net.flyingfishflash.ledger.accounts.AccountType;

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
    this.placeholder = account.getPlaceholder();
    this.hidden = account.getHidden();
    this.taxRelated = account.getTaxRelated();
    this.accountCategory = account.getAccountCategory();
    this.accountType = account.getAccountType();
  }
}