package net.flyingfishflash.ledger.foundation.authentication.web;

import java.security.Principal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.foundation.authentication.AuthenticationFacade;
import net.flyingfishflash.ledger.foundation.authentication.payload.response.SignInResponse;
import net.flyingfishflash.ledger.foundation.authentication.payload.response.SignOutResponse;
import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.UserRepository;
import net.flyingfishflash.ledger.foundation.users.service.UserService;

@RestController
@RequestMapping("/api/v1/ledger/auth")
public class BasicAuthenticationController {

  private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationController.class);

  private AuthenticationFacade authenticationFacade;

  private UserRepository userRepository;

  private PasswordEncoder encoder;

  private final UserService userService;

  public BasicAuthenticationController(
      AuthenticationFacade authenticationFacade,
      UserRepository userRepository,
      PasswordEncoder encoder,
      UserService userService) {
    this.authenticationFacade = authenticationFacade;
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.userService = userService;
  }

  @GetMapping("/signin")
  @ResponseBody
  public SignInResponse signIn(Principal principal) {

    User user = userService.findByUsername(principal.getName());

    SignInResponse signInResponse = new SignInResponse();

    signInResponse.setId(user.getId());
    signInResponse.setUsername(principal.getName());
    signInResponse.setRoles(user.getAuthorities());

    return signInResponse;
  }

  @PostMapping(value = {"/signout"})
  @ResponseBody
  public SignOutResponse signOut(HttpServletRequest request, HttpServletResponse response) {

    logger.info("Logging out");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        logger.info("cookie value: " + cookie.getValue());
        cookie.setMaxAge(0);
      }
    }

    return new SignOutResponse("Logged Out");
  }
}
