package net.flyingfishflash.ledger;

public class Account {

	private enum AccountClass {
		ROOT
		, ASSET
		, LIABILITY
		, EQUITY
		, INCOME
		, EXPENSE
	}

	private enum AccountType {
		ROOT
		, INCOME
		, EXPENSE
		, ASSET
		, LIABILITY
		, CASH
		, BANK
		, MUTUAL		
		, STOCK
		, CREDIT
		, EQUITY
	}
	
	private long id;
	private String guid;
	private String name;
	private String longname;
	private AccountClass account_class;
	private AccountType account_type;
	private String commodity_guid;
	private int parent_id;
	private String parent_guid;
	private String code;
	private String description;
	private boolean hidden;
	private boolean placeholder;
	private long lft;
	private long rgt;
	private int depth;

}
