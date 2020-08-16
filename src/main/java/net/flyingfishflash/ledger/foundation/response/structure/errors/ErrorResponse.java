package net.flyingfishflash.ledger.foundation.response.structure.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.flyingfishflash.ledger.foundation.response.structure.ResponseApiStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {

  private final String status;
  private final String message;
  private ErrorResponseStructure<T> response;

  public ErrorResponse(T object, String message, ResponseApiStatusCode responseApiStatusCode) {
    response = new ErrorResponseStructure<T>(object);
    this.status = responseApiStatusCode.name().toLowerCase();
    this.message = message;
  }

  public ErrorResponse(T object, String message) {
    response = new ErrorResponseStructure<T>(object);
    this.status = ResponseApiStatusCode.Error.name().toLowerCase();
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

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
