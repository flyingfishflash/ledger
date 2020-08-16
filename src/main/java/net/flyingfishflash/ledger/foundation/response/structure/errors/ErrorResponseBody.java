package net.flyingfishflash.ledger.foundation.response.structure.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ErrorResponseBody {

  @JsonIgnore private final UUID uniqueId;
  private final String code;
  private final String message;
  private final List<Error> errors;

  public ErrorResponseBody(
      final String code,
      final String message,
      final String domain,
      final String reason,
      final String errorMessage) {

    this.code = code;
    this.message = message;
    this.uniqueId = UUID.randomUUID();
    this.errors = List.of(new Error(domain, reason, errorMessage));
  }

  public static ErrorResponseBody fromDefaultAttributeMap(
      final Map<String, Object> defaultErrorAttributes) {
    // original attribute values are documented in
    // org.springframework.boot.web.servlet.error.DefaultErrorAttributes
    return new ErrorResponseBody(
        ((Integer) defaultErrorAttributes.get("status")).toString(),
        (String) defaultErrorAttributes.getOrDefault("message", "no message available"),
        (String) defaultErrorAttributes.getOrDefault("path", "no domain available"),
        (String) defaultErrorAttributes.getOrDefault("error", "no reason available"),
        (String) defaultErrorAttributes.get("message"));
  }

  public Map<String, Object> toAttributeMap() {
    return Map.of("ledgerError", this);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public List<Error> getErrors() {
    return errors;
  }

  @Override
  public String toString() {
    return "ErrorResponseBody{"
        + "uniqueId="
        + uniqueId
        + ", code='"
        + code
        + '\''
        + ", message='"
        + message
        + '\''
        + ", errors="
        + errors
        + '}';
  }

  private static final class Error {
    private final String domain;
    private final String reason;
    private final String message;

    public Error(final String domain, final String reason, final String message) {
      this.domain = domain;
      this.reason = reason;
      this.message = message;
    }

    public String getDomain() {
      return domain;
    }

    public String getReason() {
      return reason;
    }

    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
      return "Error{"
          + "domain='"
          + domain
          + '\''
          + ", reason='"
          + reason
          + '\''
          + ", message='"
          + message
          + '\''
          + '}';
    }
  }
}
