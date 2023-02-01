package net.flyingfishflash.ledger.foundation.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.session.InvalidSessionStrategy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.flyingfishflash.ledger.foundation.authentication.exceptions.InvalidSessionException;

public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

  private static final Logger logger = LoggerFactory.getLogger(CustomInvalidSessionStrategy.class);

  @Override
  public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    logger.debug(
        "Invalid session requested ({})detected when requesting URI: {}",
        request.getRequestURI(),
        request.getRequestedSessionId());

    throw new InsufficientAuthenticationException(
        "Full authentication is required to access this resource",
        new InvalidSessionException(request.getRequestedSessionId()));
  }
}
