package net.flyingfishflash.ledger.core.authentication.web;

import java.security.Principal;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.flyingfishflash.ledger.core.authentication.payload.response.SignInResponse;
import net.flyingfishflash.ledger.core.authentication.payload.response.SignOutResponse;
import net.flyingfishflash.ledger.core.users.service.UserService;

@Tag(name = "authentication controller - basic")
@RestController
@RequestMapping("/api/v1/ledger/auth")
public class BasicAuthenticationController {

  private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationController.class);

  private final UserService userService;

  public BasicAuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/signin")
  @ResponseBody
  public SignInResponse signIn(Principal principal) {

    var user = userService.findByUsername(principal.getName());

    var signInResponse = new SignInResponse();

    signInResponse.setId(user.getId());
    signInResponse.setUsername(principal.getName());
    signInResponse.setRoles(user.getAuthorities());

    return signInResponse;
  }

  @PostMapping(value = {"/signout"})
  @ResponseBody
  public SignOutResponse signOut(HttpServletRequest request, HttpServletResponse response) {

    logger.info("Logging out");

    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        var cookieValue = cookie.getValue();
        logger.info("cookie value: {}", cookieValue);
        cookie.setMaxAge(0);
      }
    }

    return new SignOutResponse("Logged Out");
  }
}
