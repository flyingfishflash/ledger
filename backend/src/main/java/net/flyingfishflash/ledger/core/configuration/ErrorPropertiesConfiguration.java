package net.flyingfishflash.ledger.core.configuration;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorPropertiesConfiguration {

  @Bean
  public ErrorProperties errorProperties() {
    var errorProperties = new ErrorProperties();
    errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
    errorProperties.setIncludeException(true);
    errorProperties.setIncludeBindingErrors(ErrorProperties.IncludeAttribute.ALWAYS);
    errorProperties.setIncludeStacktrace(ErrorProperties.IncludeAttribute.ALWAYS);
    return errorProperties;
  }
}
