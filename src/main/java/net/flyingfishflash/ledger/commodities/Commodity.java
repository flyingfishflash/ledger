package net.flyingfishflash.ledger.commodities;

public class Commodity {
	
	private long id;
	private String guid; // varchar(32)
    private String namespace;
	private String mnemonic;
	private String fullname;
	private String cusip;
	private boolean quote_remote;
	private String quote_remote_source;
	
}
