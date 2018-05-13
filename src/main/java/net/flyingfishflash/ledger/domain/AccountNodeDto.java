package net.flyingfishflash.ledger.domain;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

public class AccountNodeDto {

    public Long id;

    public Long lft;

    public Long rgt;

    public Long lvl;

    public Long parentId;

    public String discriminator;

    public String guid;

    public String name;

    public String longname;

    public String code;

    public String description;

    public Boolean placeholder;

    public Boolean hidden;

    public Boolean taxRelated;

    public AccountCategory accountCategory;

    public AccountType accountType;

    public AccountNodeDto(AccountNode accountNode) {

        this.id = accountNode.getId();
        this.lft = accountNode.getLeft();
        this.rgt = accountNode.getRight();
        this.lvl = accountNode.getLevel();
        this.parentId = accountNode.parent.getId();
        this.discriminator = accountNode.getDiscriminator();
        this.guid = accountNode.getGuid();
        this.name = accountNode.getName();
        this.longname = accountNode.getLongname();
        this.code = accountNode.getCode();
        this.description = accountNode.getDescription();
        this.placeholder = accountNode.getPlaceholder();
        this.hidden = accountNode.getHidden();
        this.taxRelated = accountNode.getTaxRelated();
        this.accountCategory = accountNode.getAccountCategory();
        this.accountType = accountNode.getAccountType();

    }

}
