package net.flyingfishflash.ledger.core.authentication.web;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.flyingfishflash.ledger.core.authentication.data.dto.SignInResponse;
import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.users.service.UserService;

@Tag(name = "authentication controller - basic")
@RestController
@RequestMapping("/auth")
@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
public class BasicAuthenticationController {

  private final UserService userService;

  public BasicAuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/signin")
  @ResponseBody
  @Operation(
      summary = "Sign-In",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
      })
  public Response<SignInResponse> signIn(Principal principal, HttpServletRequest request) {

    var user = userService.findByUsername(principal.getName());
    var signInResponse = new SignInResponse();
    signInResponse.setId(user.getId());
    signInResponse.setUsername(user.getUsername());
    signInResponse.setRoles(user.getAuthorities());

    return new Response<>(
        signInResponse,
        "Successful sign-in using basic authentication",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }

  @PostMapping(value = {"/signout"})
  @ResponseBody
  @Operation(
      summary = "Sign-Out",
      responses = {
        @ApiResponse(responseCode = "200"),
      })
  public Response<String> signOut(HttpServletRequest request, HttpServletResponse response) {

    var auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    ArrayList<String> cookieList = new ArrayList<>();
    Optional.ofNullable(request.getCookies()).stream()
        .flatMap(Arrays::stream)
        .forEach(
            cookie -> {
              cookieList.add(cookie.getValue());
              cookie.setMaxAge(0);
            });

    return new Response<>(
        String.format("Successful sign-out. Cookies: %s", cookieList),
        "Successful sign-out",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }
}
