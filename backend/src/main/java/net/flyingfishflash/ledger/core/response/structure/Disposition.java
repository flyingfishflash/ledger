package net.flyingfishflash.ledger.core.response.structure;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents the disposition of any request from a client.
 *
 * <p>Every response to a client must include no more than one Disposition.
 */
public enum Disposition {
  /** Client request error due to an unexpected platform or framework error */
  ERROR,
  /** Client request failure in an anticipated manner */
  FAILURE,
  /** Client request success */
  SUCCESS;

  /**
   * Returns an enum constant name() formatted in Camel Case (dromedaryCamelCase)
   *
   * @return An enum constant name() formatted in Camel Case (dromedaryCamelCase)
   */
  @JsonValue
  public String jsonValue() {
    return name().toLowerCase();
  }
}
