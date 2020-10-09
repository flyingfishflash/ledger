package net.flyingfishflash.ledger.foundation.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger =
      LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {

    logger.info("* Authentication Exception: " + exception.getLocalizedMessage());

    resolver.resolveException(request, response, null, exception);

    /*


    ObjectMapper objectMapper = new ObjectMapper();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");

    ErrorResponseBody ledgerError =
        new ErrorResponseBody(
            "custom error code",
            exception.getLocalizedMessage(),
            "error domain",
            "error reason",
            "error message");

    ErrorResponse<ErrorResponseBody> errorResponse =
        new ErrorResponse<>(ledgerError, exception.getLocalizedMessage());

    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, errorResponse);
    out.flush();
    */
  }
}
