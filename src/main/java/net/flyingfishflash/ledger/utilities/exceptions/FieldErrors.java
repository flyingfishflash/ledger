package net.flyingfishflash.ledger.utilities.exceptions;

public class FieldErrors {

  private String field;
  private String message;
  private Object rejectedValue;

  public FieldErrors() {}

  public FieldErrors(String field, Object rejectedValue, String message) {

    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public Object getRejectedValue() {
    return rejectedValue;
  }

  public void setRejectedValue(Object rejectedValue) {
    this.rejectedValue = rejectedValue;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
