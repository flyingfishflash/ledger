package net.flyingfishflash.ledger.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import net.flyingfishflash.ledger.common.IdentifierFactory;
import pl.exsio.nestedj.model.NestedNode;

@Entity
@Table(name = "account")
public class Account implements NestedNode<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @Column(name = "guid", unique = true, updatable = false)
  protected String guid;

  @Column(name = "name")
  protected String name;

  @Column(name = "longname", length = 4096)
  protected String longName;

  @Column(name = "code")
  protected String code;

  @Column(name = "description", length = 2048)
  protected String description;

  @Column(name = "placeholder")
  protected Boolean placeholder = false;

  @Column(name = "hidden")
  protected Boolean hidden = false;

  @Column(name = "tax_related")
  protected Boolean taxRelated = false;

  @Column(name = "account_class")
  @Enumerated(EnumType.STRING)
  protected AccountCategory accountCategory;

  @Column(name = "account_type")
  @Enumerated(EnumType.STRING)
  protected AccountType accountType;

  @Column(name = "tree_left", nullable = false)
  protected Long treeLeft;

  @Column(name = "tree_right", nullable = false)
  protected Long treeRight;

  @Column(name = "tree_level", nullable = false)
  protected Long treeLevel;

  @Column(name = "parent_id")
  protected Long parentId;

  @Column(name = "discriminator", nullable = false)
  protected String discriminator;

  public Account() {
    this.setGuid();
    this.setDiscriminator("account");
  }

  public Boolean isRoot() {
    return this.getTreeLeft() == 1L;
  }

  public String getGuid() {
    return guid;
  }

  private void setGuid() {
    this.guid = IdentifierFactory.getInstance().generateIdentifier();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(Boolean placeholder) {
    this.placeholder = placeholder;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public Boolean getTaxRelated() {
    return taxRelated;
  }

  public void setTaxRelated(Boolean taxRelated) {
    this.taxRelated = taxRelated;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDiscriminator() {
    return discriminator;
  }

  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public Long getTreeLeft() {
    return treeLeft;
  }

  @Override
  public void setTreeLeft(Long left) {
    this.treeLeft = left;
  }

  @Override
  public Long getTreeRight() {
    return treeRight;
  }

  @Override
  public void setTreeRight(Long right) {
    this.treeRight = right;
  }

  @Override
  public Long getTreeLevel() {
    return treeLevel;
  }

  @Override
  public void setTreeLevel(Long lvl) {
    this.treeLevel = lvl;
  }

  @Override
  public Long getParentId() {
    return parentId;
  }

  @Override
  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", guid='" + guid + '\'' +
        ", name='" + name + '\'' +
        ", longName='" + longName + '\'' +
        ", code='" + code + '\'' +
        ", description='" + description + '\'' +
        ", placeholder=" + placeholder +
        ", hidden=" + hidden +
        ", taxRelated=" + taxRelated +
        ", accountCategory=" + accountCategory +
        ", accountType=" + accountType +
        ", treeLeft=" + treeLeft +
        ", treeRight=" + treeRight +
        ", treeLevel=" + treeLevel +
        ", parentId=" + parentId +
        ", discriminator='" + discriminator + '\'' +
        '}';
  }

}
