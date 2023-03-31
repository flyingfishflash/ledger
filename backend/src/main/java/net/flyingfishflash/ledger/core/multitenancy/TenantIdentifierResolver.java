package net.flyingfishflash.ledger.core.multitenancy;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver
    implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

  public static final String COMMON = "public";
  public static final String UNDEFINED = "undefined";

  @Override
  public String resolveCurrentTenantIdentifier() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(Predicate.not(AnonymousAuthenticationToken.class::isInstance))
        .map(Principal::getName)
        .orElse(UNDEFINED);
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
  }
}
