package net.flyingfishflash.ledger.nestedset.unit.config.jpa.discriminator;

import java.util.Collections;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.MapJpaTreeDiscriminator;
import net.flyingfishflash.ledger.nestedset.unit.model.TestNode;

public class TestJpaTreeDiscriminator extends MapJpaTreeDiscriminator<Long, TestNode> {

  public TestJpaTreeDiscriminator() {
    super(Collections.singletonMap("discriminator", () -> 1));
  }
}
