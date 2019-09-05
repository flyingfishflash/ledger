package net.flyingfishflash.ledger.accounts.exceptions;

public class AccountCreateException extends RuntimeException {

  public AccountCreateException(String message) {
    super(message);
  }
  public AccountCreateException(String message, RuntimeException cause) {
    super(message, cause);
  }
}
