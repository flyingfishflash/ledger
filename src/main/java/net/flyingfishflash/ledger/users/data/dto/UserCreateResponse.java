package net.flyingfishflash.ledger.users.data.dto;

public class UserCreateResponse {
  private String message;

  public UserCreateResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}