package net.flyingfishflash.ledger.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import net.flyingfishflash.ledger.common.IdentifierFactory;

@MappedSuperclass
public class Account {
	
	@Column(name="guid", unique = true, updatable = false)
	protected String guid;

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

	Account() {
		this.setGuid();
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid() {
		guid = IdentifierFactory.getInstance().generateIdentifier();
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

}
