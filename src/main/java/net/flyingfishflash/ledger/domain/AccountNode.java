package net.flyingfishflash.ledger.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import pl.exsio.nestedj.model.NestedNode;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class AccountNode extends Account implements NestedNode<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

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

  public AccountNode() {
    super();
    this.setDiscriminator("account");
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

  public String getDiscriminator() {
    return discriminator;
  }

  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }

  public Boolean isRoot() {
    return this.getTreeLeft() == 1L;
  }

  @Override
  public String toString() {
    return "AccountNode{" +
        "id=" + id +
        ", treeLeft=" + treeLeft +
        ", treeRight=" + treeRight +
        ", treeLevel=" + treeLevel +
        ", parentId=" + parentId +
        ", discriminator='" + discriminator + '\'' +
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
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof AccountNode) {
      return (this.hashCode() == o.hashCode());
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getGuid());
  }
}
