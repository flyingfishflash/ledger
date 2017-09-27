package net.flyingfishflash.ledger.domain.legacy;

import java.time.LocalDateTime;

public class Transaction {

	private long id;
	private String guid; // varchar(32)
    private String currency_guid; // actually a commodity_guid
    private String num;
    private LocalDateTime post_date;  // timestamp without time zone
    private LocalDateTime enter_date; // timestamp without time zone
    private String description;
	
}
