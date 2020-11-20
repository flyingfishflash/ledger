package net.flyingfishflash.ledger.foundation;

import java.util.UUID;

/** The IdentifierFactory is responsible for generating unique identifiers for domain objects. */
public class IdentifierFactory {

  private static final IdentifierFactory INSTANCE;

  static {
    INSTANCE = new IdentifierFactory();
  }

  private IdentifierFactory() {}

  /**
   * Returns a singleton instance of the IdentifierFactory.
   *
   * @return the IdentifierFactory instance.
   */
  public static IdentifierFactory getInstance() {
    return INSTANCE;
  }

  /**
   * Generate a unique identifier for use by domain objects providing randomly chosen <code>
   * java.util.UUID</code>s.
   *
   * @return a String representation of a unique identifier.
   */
  public String generateIdentifier() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
