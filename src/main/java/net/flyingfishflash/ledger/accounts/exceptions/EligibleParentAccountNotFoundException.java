package net.flyingfishflash.ledger.accounts.exceptions;

import org.springframework.http.HttpStatus;

public class EligibleParentAccountNotFoundException extends AccountException {

  public EligibleParentAccountNotFoundException(String guid) {
    super(
        HttpStatus.NOT_FOUND,
        "No accounts eligible to be a direct parent of account guid " + guid);
  }

  public EligibleParentAccountNotFoundException(Long id) {
    super(HttpStatus.NOT_FOUND, "No accounts elligible to be a direct parent of account id " + id);
  }

  public EligibleParentAccountNotFoundException(Long id, String context) {
    super(
        HttpStatus.NOT_FOUND,
        "No accounts eligible to be a direct parent of account id " + id + ". " + context);
  }

  public EligibleParentAccountNotFoundException(String guid, String context) {
    super(
        HttpStatus.NOT_FOUND,
        "No accounts eligible to be a direct parent of account guid " + guid + ". " + context);
  }
}
