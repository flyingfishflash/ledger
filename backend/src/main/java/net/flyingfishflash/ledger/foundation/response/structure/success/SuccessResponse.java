package net.flyingfishflash.ledger.foundation.response.structure.success;

import net.flyingfishflash.ledger.foundation.response.structure.ApiStatusCode;

public class SuccessResponse<T> {

  private final String status;
  private SuccessResponseStructure<T> response;

  public SuccessResponse(T object) {
    this.response = new SuccessResponseStructure<>(object);
    this.status = ApiStatusCode.SUCCESS.name().toLowerCase();
  }

  public SuccessResponse(T object, String message) {
    this.response = new SuccessResponseStructure<>(object, message);
    this.status = ApiStatusCode.SUCCESS.name().toLowerCase();
  }

  public SuccessResponse(T object, Integer length, String message) {
    this.response = new SuccessResponseStructure<>(object, length, message);
    this.status = ApiStatusCode.SUCCESS.name().toLowerCase();
  }

  public SuccessResponse(T object, Integer length) {
    this.response = new SuccessResponseStructure<>(object, length);
    this.status = ApiStatusCode.SUCCESS.name().toLowerCase();
  }

  public SuccessResponseStructure<T> getResponse() {
    return response;
  }

  public void setResponse(SuccessResponseStructure<T> response) {
    this.response = response;
  }

  public String getStatus() {
    return status;
  }
}
