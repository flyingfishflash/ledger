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

import pl.exsio.nestedj.model.NestedNode;

import net.flyingfishflash.ledger.commodities.data.Commodity;

@Entity
public class Account implements NestedNode<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SuppressWarnings("java:S1845")
  private Long id;

  @Column(unique = true, updatable = false)
  private String guid;

  @Column(length = 128)
  private String name;

  @Column(length = 4096)
  private String longName;

  @Column(length = 128)
  private String code;

  @Column(length = 2048)
  private String description;

  @Column(length = 4096)
  private String note;

  private Boolean placeholder = false;

  private Boolean hidden = false;

  private Boolean taxRelated = false;

  @Enumerated(EnumType.STRING)
  private AccountCategory category;

  @Enumerated(EnumType.STRING)
  private AccountType type;

  @Enumerated(EnumType.STRING)
  private NormalBalance normalBalance;

  @Column(length = 10)
  private String currency;

  @ManyToOne
  @JoinColumn(name = "commodity_id")
  private Commodity commodity;

  @Column(nullable = false)
  private Long treeLeft;

  @Column(nullable = false)
  private Long treeRight;

  @Column(nullable = false)
  private Long treeLevel;

  //  @Column(name = "parent_id")
  private Long parentId;

  @Column(nullable = false)
  private String discriminator;

  public Account() {
    this.setDiscriminator("account");
  }

  public Account(String guid) {
    this();
    this.guid = guid;
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

  public AccountCategory getCategory() {
    return category;
  }

  public AccountType getType() {
    return type;
  }

  public void setType(AccountType accountType) {
    this.type = accountType;
    this.category = this.type.getAccountCategory();
    this.normalBalance = this.category.getNormalBalance();
  }

  public NormalBalance getNormalBalance() {
    return normalBalance;
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
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

  @Override
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
        + ", note='"
        + note
        + '\''
        + ", placeholder="
        + placeholder
        + ", hidden="
        + hidden
        + ", taxRelated="
        + taxRelated
        + ", category="
        + category
        + ", type="
        + type
        + ", normalBalance="
        + normalBalance
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
