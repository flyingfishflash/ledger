package net.flyingfishflash.ledger.foundation.response.structure.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorResponseBody {

  @JsonIgnore private final UUID uniqueId;
  // class name of the exception
  private final String exception;
  // message provided by the exception
  private final String message;
  // either binding errors on individual fields, or other root cause for the provided exception
  private final List<ErrorResponseBodyItem> causes;

  public ErrorResponseBody(Throwable exception) {
    this.exception = exception.getClass().getSimpleName();
    this.message = exception.getLocalizedMessage();
    this.uniqueId = UUID.randomUUID();
    this.causes = extractCauses(exception);
  }

  public ErrorResponseBody(Throwable exception, String message) {
    this.exception = exception.getClass().getSimpleName();
    this.message = message;
    this.uniqueId = UUID.randomUUID();
    this.causes = extractCauses(exception);
  }

  public ErrorResponseBody(
      Throwable exception, String message, List<ErrorResponseBodyItem> errorResponseBodyItems) {
    this.exception = exception.getClass().getSimpleName();
    this.message = message;
    this.uniqueId = UUID.randomUUID();
    this.causes = errorResponseBodyItems;
  }

  private List<ErrorResponseBodyItem> extractCauses(Throwable exception) {

    ErrorResponseBodyItem errorResponseBodyItem;
    List<ErrorResponseBodyItem> errorResponseBodyItems = new ArrayList<>();

    if (exception.getCause() != null) {
      Throwable rootCause = exceptionCause(exception);
      errorResponseBodyItem =
          new ErrorResponseBodyItem(
              rootCause.getClass().getSimpleName(), rootCause.getLocalizedMessage());
      errorResponseBodyItems.add(errorResponseBodyItem);
    }

    return errorResponseBodyItems;
  }

  private static Throwable exceptionCause(Throwable throwable) {
    Objects.requireNonNull(throwable);
    var rootCause = throwable;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
      rootCause = rootCause.getCause();
    }
    return rootCause;
  }

  public Map<String, Object> toAttributeMap() {
    return Map.of("ledgerError", this);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getException() {
    return exception;
  }

  public String getMessage() {
    return message;
  }

  public List<ErrorResponseBodyItem> getCauses() {
    return causes;
  }

  @Override
  public String toString() {
    return "ErrorResponseBody{"
        + "uniqueId="
        + uniqueId
        + ", code='"
        + exception
        + '\''
        + ", message='"
        + message
        + '\''
        + ", causes="
        + causes
        + '}';
  }
}
