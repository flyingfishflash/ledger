package net.flyingfishflash.ledger.accounts.data;

import java.util.Collections;
import pl.exsio.nestedj.config.jpa.discriminator.MapJpaTreeDiscriminator;

public class AccountTreeDiscriminator extends MapJpaTreeDiscriminator<Long, Account> {

  public AccountTreeDiscriminator() {
    super(Collections.singletonMap("discriminator", () -> "account"));
  }
}
