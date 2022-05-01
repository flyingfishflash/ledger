package net.flyingfishflash.ledger.foundation.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger =
      LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  @Autowired
  @Qualifier("handlerExceptionResolver") private HandlerExceptionResolver resolver;

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {

    logger.info("* Authentication Exception: {}", exception.getLocalizedMessage());

    resolver.resolveException(request, response, null, exception);
  }
}
