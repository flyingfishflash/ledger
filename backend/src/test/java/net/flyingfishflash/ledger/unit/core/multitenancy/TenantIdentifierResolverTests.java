package net.flyingfishflash.ledger.unit.core.multitenancy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import net.flyingfishflash.ledger.core.multitenancy.TenantIdentifierResolver;

class TenantIdentifierResolverTests {

  TenantIdentifierResolver tenantIdentifierResolver = new TenantIdentifierResolver();

  @Test
  @DisplayName("validateExistingCurrentSessions() Returns True")
  void validateExistingCurrentSessionsReturnsTrue() {
    assertThat(tenantIdentifierResolver.validateExistingCurrentSessions()).isTrue();
  }

  @Test
  @DisplayName("resolveCurrentTenantIdentifier() Returns Mocked Username")
  void resolveCurrentTenantIdentifierReturnsMockedUsername() {

    Authentication authentication = Mockito.mock(Authentication.class);
    when(authentication.getName()).thenReturn("lorem ipsum");
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    assertThat(tenantIdentifierResolver.resolveCurrentTenantIdentifier()).isEqualTo("lorem ipsum");
  }

  @Test
  @DisplayName(
      "resolveCurrentTenantIdentifier() Returns UNDEFINED When Authentication is Anonymous")
  void resolveCurrentTenantIdentifierReturnsUndefinedWhenAuthenticationIsAnonymous() {

    Authentication authentication = Mockito.mock(AnonymousAuthenticationToken.class);
    when(authentication.getName()).thenReturn("lorem ipsum");
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    assertThat(tenantIdentifierResolver.resolveCurrentTenantIdentifier())
        .isEqualTo(TenantIdentifierResolver.UNDEFINED);
  }

  @Test
  @DisplayName("resolveCurrentTenantIdentifier() Returns UNDEFINED When Authentication is Null")
  void resolveCurrentTenantIdentifierReturnsUndefinedWhenAuthenticationIsNull() {

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextHolder.setContext(securityContext);
    assertThat(tenantIdentifierResolver.resolveCurrentTenantIdentifier())
        .isEqualTo(TenantIdentifierResolver.UNDEFINED);
  }
}
