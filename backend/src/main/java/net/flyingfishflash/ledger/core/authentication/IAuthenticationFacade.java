package net.flyingfishflash.ledger.core.authentication;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
  Authentication getAuthentication();
}
