package net.flyingfishflash.ledger.domain;

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
	
	private long id;                     // bigint
	private String guid;                 // character varying(32)
	private String name;                 // character varying(2048)
	private String longname;             // character varying(2048)
	private AccountClass account_class;
	private AccountType account_type;
	private String commodity_guid;       // character varying(32)
	private int parent_id;               // bigint
	private String parent_guid;          // character varying(32)
	private String code;                 // character varying(2048)
	private String description;          // character varying(2048)
	private boolean hidden;
	private boolean placeholder;
	private long lft;                    // bigint 
	private long rgt;                    // bigint
	private int depth;                   // bigint 
	

}
