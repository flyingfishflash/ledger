package net.flyingfishflash.ledger.foundation.users.data.dto;

public abstract class ApiMessage {
  private String message;

  protected ApiMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
