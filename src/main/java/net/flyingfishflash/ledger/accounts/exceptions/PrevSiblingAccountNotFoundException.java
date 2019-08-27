package net.flyingfishflash.ledger.accounts.exceptions;

public class PrevSiblingAccountNotFoundException extends RuntimeException {

  public PrevSiblingAccountNotFoundException(String accountLongName) {
    super("Account '" + accountLongName + "' has no previous sibling account");
  }
}
