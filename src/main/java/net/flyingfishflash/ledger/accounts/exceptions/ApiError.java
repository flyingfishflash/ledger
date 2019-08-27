package net.flyingfishflash.ledger.accounts.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ApiError {
  private LocalDateTime timestamp;
  private String path;
  private int status;
  private HttpStatus error;
  private String exception;
  private List<String> messages;

  public ApiError(
      String path, int status, HttpStatus error, String exception, List<String> messages) {
    this.timestamp = LocalDateTime.now();
    this.path = path;
    this.status = status;
    this.error = error;
    this.exception = exception;
    this.messages = messages;
  }

  public int getStatus() {
    return status;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public HttpStatus getError() {
    return error;
  }

  public String getPath() {
    return path;
  }

  public String getException() {
    return exception;
  }

  public List<String> getMessages() {
    return messages;
  }

  @Override
  public String toString() {
    return "ApiError{" +
        "timestamp=" + timestamp +
        ", path='" + path + '\'' +
        ", status=" + status +
        ", error=" + error +
        ", exception='" + exception + '\'' +
        ", messages=" + messages +
        '}';
  }
}
