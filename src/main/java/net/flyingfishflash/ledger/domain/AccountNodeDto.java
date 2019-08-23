package net.flyingfishflash.ledger.domain;

public class AccountNodeDto {

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

  public AccountNodeDto(AccountNode accountNode) {

    this.id = accountNode.getId();
    this.treeLeft = accountNode.getTreeLeft();
    this.treeRight = accountNode.getTreeRight();
    this.treeLevel = accountNode.getTreeLevel();
    this.parentId = accountNode.getParentId();
    this.discriminator = accountNode.getDiscriminator();
    this.guid = accountNode.getGuid();
    this.name = accountNode.getName();
    this.longName = accountNode.getLongName();
    this.code = accountNode.getCode();
    this.description = accountNode.getDescription();
    this.placeholder = accountNode.getPlaceholder();
    this.hidden = accountNode.getHidden();
    this.taxRelated = accountNode.getTaxRelated();
    this.accountCategory = accountNode.getAccountCategory();
    this.accountType = accountNode.getAccountType();
  }
}
