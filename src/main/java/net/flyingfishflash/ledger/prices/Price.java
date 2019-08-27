package net.flyingfishflash.ledger.prices;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price {

	private long id;
	private String guid;            // character varying(32)
	private String commodity_guid;  // character varying(32)
	private String currency_guid;   // character varying(32)
	private LocalDateTime date;     // timestamp without time zone
	private String price_source;    // character varying(2048)
	private String price_type;      // character varying(2048)
	private BigDecimal price;       // numeric(18,8)

}
