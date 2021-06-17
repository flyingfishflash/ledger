package net.flyingfishflash.ledger.foundation.response.structure.errors;

import com.fasterxml.jackson.annotation.JsonInclude;

import net.flyingfishflash.ledger.foundation.response.structure.ApiStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {

  private final String status;
  // private final String message;
  private ErrorResponseStructure<T> response;

  public ErrorResponse(T object /*, String message*/, ApiStatusCode apiStatusCode) {
    response = new ErrorResponseStructure<>(object);
    this.status = apiStatusCode.name().toLowerCase();
    // this.message = message;
  }

  public ErrorResponse(T object /*, String message*/) {
    response = new ErrorResponseStructure<>(object);
    this.status = ApiStatusCode.ERROR.name().toLowerCase();
    // this.message = message;
  }

  // public String getMessage() {
  // return message;
  // }

  public ErrorResponseStructure<T> getResponse() {
    return response;
  }

  public void setResponse(ErrorResponseStructure<T> response) {
    this.response = response;
  }

  public String getStatus() {
    return status;
  }
}
