package net.flyingfishflash.ledger.accounts.exceptions;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(String guid) {
    super("Account not found for id " + guid);
  }

  public AccountNotFoundException(Long id) {
    super("Account not found for id " + id);
  }

  public AccountNotFoundException(Long id, String context) {
    super("Account not found for id " + id + ". " + context);
  }

  public AccountNotFoundException(String guid, String context) {
    super("Account not found for guid " + guid + ". " + context);
  }
}
