package net.flyingfishflash.ledger.accounts.exceptions;

public class EligibleParentAccountNotFoundException extends RuntimeException {

  public EligibleParentAccountNotFoundException(String guid) {
    super("No accounts elligible to be a direct parent of account guid " + guid);
  }

  public EligibleParentAccountNotFoundException(Long id) {
    super("No accounts elligible to be a direct parent of account id " + id);
  }

  public EligibleParentAccountNotFoundException(Long id, String context) {
    super("No accounts elligible to be a direct parent of account id " + id + ". " + context);
  }

  public EligibleParentAccountNotFoundException(String guid, String context) {
    super("No accounts elligible to be a direct parent of account guid " + guid + ". " + context);
  }

}
