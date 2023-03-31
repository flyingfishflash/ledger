package net.flyingfishflash.ledger.unit.core.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.security.Principal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.Cookie;

import net.flyingfishflash.ledger.core.authentication.data.dto.SignInResponse;
import net.flyingfishflash.ledger.core.authentication.web.BasicAuthenticationController;
import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.users.data.Role;
import net.flyingfishflash.ledger.core.users.data.User;
import net.flyingfishflash.ledger.core.users.service.UserService;

/**
 * Unit tests for {@link
 * net.flyingfishflash.ledger.core.authentication.web.BasicAuthenticationController}
 *
 * <p>Testing the various method response objects directly without filtering through controller
 * advice or serialization
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BasicAuthenticationController")
class BasicAuthenticationControllerTests {

  @Mock private UserService mockUserService;
  @Mock private Principal mockPrincipal;
  @InjectMocks BasicAuthenticationController basicAuthenticationController;

  private static MockHttpServletRequest mockRequest;
  private static MockHttpServletResponse mockResponse;

  @BeforeEach
  void setup() {
    mockRequest = new MockHttpServletRequest("Ignored", "Ignored");
    mockResponse = new MockHttpServletResponse();
  }

  @Test
  void signIn() {
    var user = new User("Username", "Password", "Email@Email", "First Name", "Last Name");
    user.setId(999L);
    user.grantAuthority(Role.ROLE_VIEWER);
    var signInResponse = new SignInResponse();
    signInResponse.setId(user.getId());
    signInResponse.setUsername(user.getUsername());
    signInResponse.setRoles(user.getAuthorities());
    when(mockPrincipal.getName()).thenReturn("Lorem Ipsum");
    when(mockUserService.findByUsername(anyString())).thenReturn(user);
    assertThat(basicAuthenticationController.signIn(mockPrincipal, mockRequest))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(new Response<>(signInResponse, "Ignored", "Ignored", URI.create("Ignored")));
  }

  @Test
  void signOut() {
    mockRequest.setCookies(new Cookie("LoremIpsum", "Lorem Ipsum"));
    assertThat(basicAuthenticationController.signOut(mockRequest, mockResponse))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(
                "Successful sign-out. Cookies: [Lorem Ipsum]",
                "Ignored",
                "Ignored",
                URI.create("Ignored")));

    mockRequest.setCookies();
    assertThat(basicAuthenticationController.signOut(mockRequest, mockResponse))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(
                "Successful sign-out. Cookies: []", "Ignored", "Ignored", URI.create("Ignored")));

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(Mockito.mock(Authentication.class));
    SecurityContextHolder.setContext(securityContext);
    mockRequest.setCookies();
    assertThat(basicAuthenticationController.signOut(mockRequest, mockResponse))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(
                "Successful sign-out. Cookies: []", "Ignored", "Ignored", URI.create("Ignored")));
  }
}
