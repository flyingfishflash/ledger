package net.flyingfishflash.ledger.foundation.authentication;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
  Authentication getAuthentication();
}
