package net.flyingfishflash.ledger.core;

public final class Messages {

  private Messages() {}

  public enum Error {
    NULL_CONSTRUCTOR_ARG("Constructor argument(s) must not be null"),
    NULL_METHOD_ARG("Method argument(s) must not be null"),
    CANNOT_INSTANTIATE_CLASS("Class cannot be instantiated");

    private final String value;

    Error(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }
  }
}
