package net.flyingfishflash.ledger.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.flyingfishflash.ledger.core.authentication.web.BasicAuthenticationController;
import net.flyingfishflash.ledger.core.users.web.UserController;
import net.flyingfishflash.ledger.domain.accounts.web.AccountController;
import net.flyingfishflash.ledger.domain.books.web.BookController;
import net.flyingfishflash.ledger.domain.importer.web.GnucashFileImportController;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  @Value("${config.application.api-v1-url-path}")
  public String apiPrefixV1;

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix(
        apiPrefixV1,
        // TODO: Replace with Custom Annotation
        HandlerTypePredicate.forBasePackageClass(
            AccountController.class,
            BasicAuthenticationController.class,
            BookController.class,
            GnucashFileImportController.class,
            UserController.class));
  }
}
