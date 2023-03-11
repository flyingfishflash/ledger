package net.flyingfishflash.ledger.core.utilities;

import java.util.UUID;

import net.flyingfishflash.ledger.core.Messages;

public class IdentifierUtility {

  @SuppressWarnings("java:S1118")
  public IdentifierUtility() {
    throw new UnsupportedOperationException(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  /**
   * Generate a unique identifier for use by domain objects providing randomly chosen <code>
   * java.util.UUID</code>s.
   *
   * @return a String representation of a unique identifier.
   */
  public static String identifier() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  /**
   * Generate a unique identifier for use by domain objects providing randomly chosen <code>
   * java.util.UUID</code>s.
   *
   * @return a String representation of a unique identifier.
   */
  public static String identifierWithHyphens() {
    return UUID.randomUUID().toString();
  }
}
