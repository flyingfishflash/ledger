package net.flyingfishflash.ledger.domain;

public class AccountDto {

  public Long id;

  public Long treeLeft;

  public Long treeRight;

  public Long treeLevel;

  public Long parentId;

  public String discriminator;

  public String guid;

  public String name;

  public String longName;

  public String code;

  public String description;

  public Boolean placeholder;

  public Boolean hidden;

  public Boolean taxRelated;

  public AccountCategory accountCategory;

  public AccountType accountType;

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
