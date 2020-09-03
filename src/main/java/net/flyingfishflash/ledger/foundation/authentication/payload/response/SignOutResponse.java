package net.flyingfishflash.ledger.foundation.authentication.payload.response;

public class SignOutResponse {
  private String message;

  public SignOutResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}