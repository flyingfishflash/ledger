package net.flyingfishflash.ledger.foundation.response.structure.errors;

public class ErrorResponseBodyItem {
  private final String reason;
  private final String message;

  public ErrorResponseBodyItem(final String reason, final String message) {
    this.reason = reason;
    this.message = message;
  }

  public String getReason() {
    return reason;
  }

  public String getMessage() {
    return message;
  }

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
