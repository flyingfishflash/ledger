package net.flyingfishflash.ledger.domain;

import com.google.common.base.MoreObjects;
import pl.exsio.nestedj.annotation.LeftColumn;
import pl.exsio.nestedj.annotation.LevelColumn;
import pl.exsio.nestedj.annotation.ParentColumn;
import pl.exsio.nestedj.annotation.RightColumn;
import pl.exsio.nestedj.model.NestedNode;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class AccountNode extends Account implements NestedNode<AccountNode> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "name")
    protected String name;

    @Column(name = "longname", length = 4096)
    protected String longname;
    
    @LeftColumn
    @Column(name = "tree_left", nullable = false)
    protected Long lft;

    @RightColumn
    @Column(name = "tree_right", nullable = false)
    protected Long rgt;

    @LevelColumn
    @Column(name = "tree_level", nullable = false)
    protected Long lvl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", nullable = true)
    @ParentColumn
    protected AccountNode parent;

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

	public String getName() {
        return name;
    }

	public String getLongname() {
		return longname;
	}

    @Override
    public Long getLeft() {
        return lft;
    }

    @Override
    public Long getRight() {
        return rgt;
    }

    @Override
    public Long getLevel() {
        return lvl;
    }

    @Override
    public AccountNode getParent() {
        return parent;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setName(String name) {
		this.name = name;
	}

	public void setLongname(String longname) {
		this.longname = longname;
	}

    @Override
    public void setLeft(Long left) {
        this.lft = left;
    }

    @Override
    public void setRight(Long right) {
        this.rgt = right;
    }

    @Override
    public void setLevel(Long lvl) {
        this.lvl = lvl;
    }

    @Override
    public void setParent(AccountNode parent) {
        this.parent = parent;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public Boolean isRoot( ) {
    	return this.getLeft() == 1L;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("guid", guid)
                .add("name", name)
                .add("lft", lft)
                .add("rgt", rgt)
                .add("level", lvl)
                .add("parent", parent != null ? parent.getId() : "null")
                .add("discriminator", discriminator)
                .toString();
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
