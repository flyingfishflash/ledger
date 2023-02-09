package net.flyingfishflash.ledger.domain.accounts.data;

import jakarta.persistence.*;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.commodities.data.Commodity;

@Entity
public class Account implements NestedNode<Long> {

  @Id
  @SequenceGenerator(name = "account_id_seq", sequenceName = "account_seq", allocationSize = 1)
  @GeneratedValue(generator = "account_id_seq")
  @SuppressWarnings("java:S1845")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @Enumerated(EnumType.STRING)
  private AccountCategory category;

  @Column(length = 128)
  private String code;

  @ManyToOne
  @JoinColumn(name = "commodity_id")
  private Commodity commodity;

  @Column(length = 10)
  private String currency;

  @Column(length = 2048)
  private String description;

  @Column(unique = true, updatable = false)
  private String guid;

  private Boolean hidden = false;

  @Column(length = 4096)
  private String longName;

  @Column(length = 128)
  private String name;

  @Enumerated(EnumType.STRING)
  private NormalBalance normalBalance;

  @Column(length = 4096)
  private String note;

  //  @Column(name = "parent_id")
  private Long parentId;

  private Boolean placeholder = false;

  private Boolean taxRelated = false;

  @Column(nullable = false)
  private Long treeLeft;

  @Column(nullable = false)
  private Long treeLevel;

  @Column(nullable = false)
  private Long treeRight;

  @Enumerated(EnumType.STRING)
  private AccountType type;

  public Account() {}

  public Account(String guid) {
    this();
    this.guid = guid;
  }

  public Account(String guid, Book book) {
    this();
    this.guid = guid;
    this.book = book;
  }

  public Boolean isRootNode() {
    return this.getParentId() == null;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public AccountCategory getCategory() {
    return category;
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public NormalBalance getNormalBalance() {
    return normalBalance;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Override
  public Long getParentId() {
    return parentId;
  }

  @Override
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

  @Override
  public Long getTreeLeft() {
    return treeLeft;
  }

  @Override
  public void setTreeLeft(Long left) {
    this.treeLeft = left;
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
  public Long getTreeRight() {
    return treeRight;
  }

  @Override
  public void setTreeRight(Long right) {
    this.treeRight = right;
  }

  public AccountType getType() {
    return type;
  }

  public void setType(AccountType accountType) {
    this.type = accountType;
    this.category = this.type.getAccountCategory();
    this.normalBalance = this.category.getNormalBalance();
  }

  @Override
  public String toString() {
    return "Account{"
        + "id="
        + id
        + ", book="
        + book
        + ", category="
        + category
        + ", code='"
        + code
        + '\''
        + ", commodity="
        + commodity
        + ", currency='"
        + currency
        + '\''
        + ", description='"
        + description
        + '\''
        + ", guid='"
        + guid
        + '\''
        + ", hidden="
        + hidden
        + ", longName='"
        + longName
        + '\''
        + ", name='"
        + name
        + '\''
        + ", normalBalance="
        + normalBalance
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
        + ", type="
        + type
        + '}';
  }
}
