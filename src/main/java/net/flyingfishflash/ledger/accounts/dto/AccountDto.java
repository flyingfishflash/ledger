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

  @Size(min = 1, max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "The account code may be null. It must not be an empty string, or consist only of spaces.")
  public String code;

  @Size(min = 1, max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "The account description may be null. It must not be an empty string, or consist only of spaces.")
  public String description;

  @Size(min = 1, max = 2048)
  @NotBlank
  public String discriminator;

  @Size(min = 1, max = 2048)
  @NotBlank
  public String guid;

  @NotNull public Boolean hidden;

  @Positive @NotNull public Long id;

  @Size(min = 1, max = 2048)
  @NotBlank
  public String longName;

  @Size(min = 1, max = 2048)
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
