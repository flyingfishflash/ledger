package net.flyingfishflash.ledger.integration.domain.accounts.nestedset.config.jpa.discriminator;

import java.util.Collections;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.MapJpaTreeDiscriminator;
import net.flyingfishflash.ledger.integration.domain.accounts.nestedset.model.TestNode;

public class TestJpaTreeDiscriminator extends MapJpaTreeDiscriminator<Long, TestNode> {

  public TestJpaTreeDiscriminator() {
    super(Collections.singletonMap("discriminator", () -> 1));
  }
}
