package net.flyingfishflash.ledger.accounts.exceptions;

public class NextSiblingAccountNotFoundException extends RuntimeException {

  public NextSiblingAccountNotFoundException(String accountLongName) {
    super("Account '" + accountLongName + "' has no next sibling account.");
  }
}
