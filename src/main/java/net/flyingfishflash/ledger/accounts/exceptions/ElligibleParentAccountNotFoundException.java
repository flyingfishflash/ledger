package net.flyingfishflash.ledger.accounts.exceptions;

public class ElligibleParentAccountNotFoundException extends RuntimeException {

  public ElligibleParentAccountNotFoundException(String guid) {
    super("No accounts elligible to be a direct parent of account guid " + guid);
  }

  public ElligibleParentAccountNotFoundException(Long id) {
    super("No accounts elligible to be a direct parent of account id " + id);
  }

  public ElligibleParentAccountNotFoundException(Long id, String context) {
    super("No accounts elligible to be a direct parent of account id " + id + ". " + context);
  }

  public ElligibleParentAccountNotFoundException(String guid, String context) {
    super("No accounts elligible to be a direct parent of account guid " + guid + ". " + context);
  }

}
