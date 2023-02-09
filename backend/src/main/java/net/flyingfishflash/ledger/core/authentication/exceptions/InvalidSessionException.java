package net.flyingfishflash.ledger.core.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidSessionException extends AuthenticationException {

  public InvalidSessionException(String sessionId) {
    super("Requested session id " + sessionId + " is invalid");
  }
}
