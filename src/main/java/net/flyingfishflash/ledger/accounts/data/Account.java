package net.flyingfishflash.ledger.accounts.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import net.flyingfishflash.ledger.commodities.data.Commodity;
import pl.exsio.nestedj.model.NestedNode;

@Entity
@Table(name = "account")
public class Account implements NestedNode<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "guid", unique = true, updatable = false)
  private String guid;

  @Column(name = "name")
  private String name;

  @Column(name = "longname", length = 4096)
  private String longName;

  @Column(name = "code")
  private String code;

  @Column(name = "description", length = 2048)
  private String description;

  @Column(name = "note", length = 4096)
  private String note;

  @Column(name = "placeholder")
  private Boolean placeholder = false;

  @Column(name = "hidden")
  private Boolean hidden = false;

  @Column(name = "tax_related")
  private Boolean taxRelated = false;

  @Column(name = "account_class")
  @Enumerated(EnumType.STRING)
  private AccountCategory accountCategory;

  @Column(name = "account_type")
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @ManyToOne
  @JoinColumn(name = "commodity_id")
  private Commodity commodity;

  @Column(name = "tree_left", nullable = false)
  private Long treeLeft;

  @Column(name = "tree_right", nullable = false)
  private Long treeRight;

  @Column(name = "tree_level", nullable = false)
  private Long treeLevel;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "discriminator", nullable = false)
  private String discriminator;

  public Account() {
    this.setDiscriminator("account");
  }

  public Account(String guid) {
    this.guid = guid;
    this.setDiscriminator("account");
  }

  public Boolean isRootNode() {
    return this.getParentId() == null;
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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

  public Commodity getCommodity() {
    return commodity;
  }

  public void setCommodity(Commodity commodity) {
    this.commodity = commodity;
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

  public void setId(Long id) {
    this.id = id;
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
    return "Account{"
        + "id="
        + id
        + ", guid='"
        + guid
        + '\''
        + ", name='"
        + name
        + '\''
        + ", longName='"
        + longName
        + '\''
        + ", code='"
        + code
        + '\''
        + ", description='"
        + description
        + '\''
        + ", placeholder="
        + placeholder
        + ", hidden="
        + hidden
        + ", taxRelated="
        + taxRelated
        + ", accountCategory="
        + accountCategory
        + ", accountType="
        + accountType
        + ", commodity="
        + commodity
        + ", treeLeft="
        + treeLeft
        + ", treeRight="
        + treeRight
        + ", treeLevel="
        + treeLevel
        + ", parentId="
        + parentId
        + ", discriminator='"
        + discriminator
        + '\''
        + '}';
  }
}
