package net.flyingfishflash.ledger.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Component
public class HttpSessionListenerLogging implements HttpSessionListener {

  private static final Logger logger = LoggerFactory.getLogger(HttpSessionListenerLogging.class);

  @Override
  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    logger.info("* sessionCreated: sessionId = {}", httpSessionEvent.getSession().getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    logger.info("* sessionDestroyed: sessionId = {}", httpSessionEvent.getSession().getId());
  }
}
