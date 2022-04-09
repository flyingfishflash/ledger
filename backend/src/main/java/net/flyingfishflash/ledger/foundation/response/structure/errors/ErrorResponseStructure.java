package net.flyingfishflash.ledger.foundation.response.structure.errors;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseStructure<T> implements java.io.Serializable {

  @SuppressWarnings("java:S1948")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T body;

  public ErrorResponseStructure(T body) {
    this.body = body;
  }

  public T getBody() {
    return body;
  }

  public void setBody(T body) {
    this.body = body;
  }
}
