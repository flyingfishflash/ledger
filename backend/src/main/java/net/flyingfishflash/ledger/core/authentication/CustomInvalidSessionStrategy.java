package net.flyingfishflash.ledger.core.authentication;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.session.InvalidSessionStrategy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.flyingfishflash.ledger.core.authentication.exceptions.InvalidSessionException;

public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

  @Override
  public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) {

    throw new InsufficientAuthenticationException(
        "Full authentication is required to access this resource",
        new InvalidSessionException(request.getRequestedSessionId()));
  }
}
