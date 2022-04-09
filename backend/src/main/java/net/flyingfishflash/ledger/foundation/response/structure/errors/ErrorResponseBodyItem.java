package net.flyingfishflash.ledger.foundation.response.structure.errors;

public record ErrorResponseBodyItem(String reason, String message) {

  @Override
  public String toString() {
    return "ErrorResponseBodyItem{"
        + "reason='"
        + reason
        + '\''
        + ", message='"
        + message
        + '\''
        + '}';
  }
}
