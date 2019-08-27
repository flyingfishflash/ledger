package net.flyingfishflash.ledger.splits;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Split {
	
	private long id;
	private String guid; // character varying (32)
	private String tx_guid;
	private String memo;
	private String action;
	private String reconcile_state;
	private LocalDateTime reconcile_date; // timestamp without time zone
	private long value_num;
	private long value_denom;
	private long quantity_num;
	private long quantity_denom;
	private BigDecimal value;
	private BigDecimal quantity;
	private LocalDateTime post_date;  // timestamp without time zone
	private LocalDateTime enter_date; // timestamp without time zone
	private String description;

}
