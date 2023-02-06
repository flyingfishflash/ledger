package net.flyingfishflash.ledger.foundation.response.structure;

/**
 * Represents the disposition of any request from a client.
 *
 * <p>Every response to a client must include no more than one ApiStatus.
 */
public enum ApiStatus {
  /** Applies when a client request fails due to an unexpected platform or framework error */
  ERROR,
  /** Applies when a client request fails in an anticipated manner */
  FAIL,
  /** Applies when a client request succeeds */
  SUCCESS
}
