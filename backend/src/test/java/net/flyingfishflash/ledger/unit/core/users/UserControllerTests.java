package net.flyingfishflash.ledger.unit.core.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.users.data.Role;
import net.flyingfishflash.ledger.core.users.data.User;
import net.flyingfishflash.ledger.core.users.data.dto.*;
import net.flyingfishflash.ledger.core.users.service.UserService;
import net.flyingfishflash.ledger.core.users.web.UserController;

/**
 * Unit tests for {@link UserController}
 *
 * <p>Testing the various method response objects directly without filtering through controller
 * advice or serialization
 */
@DisplayName("UserController")
@ExtendWith(MockitoExtension.class)
class UserControllerTests {

  @Mock private UserService mockUserService;
  @Mock private Principal mockPrincipal;
  @InjectMocks UserController userController;

  private static final MockHttpServletRequest mockRequest =
      new MockHttpServletRequest("Lorem Ipsum", "lorem/ipsum");

  @Test
  void getUsers() {
    var user = new User("Username", "Password", "Email@Email", "First Name", "Last Name");
    user.grantAuthority(Role.ROLE_VIEWER);
    List<User> userList = List.of(user);
    given(mockUserService.findAllUsers()).willReturn(userList);

    assertThat(userController.findAll(mockRequest))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(new Response<>(userList, "Ignored", "Ignored", URI.create("Ignored")));
  }

  @Test
  void getProfileByUsername() {
    var userProfileResponse = new UserProfileResponse(2L, "Email", "First Name", "Last Name", null);
    given(mockPrincipal.getName()).willReturn("Any Principal");
    given(mockUserService.profileByUsername(anyString())).willReturn(userProfileResponse);

    assertThat(userController.profileByUsername(mockPrincipal, mockRequest))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(userProfileResponse, "Ignored", "Ignored", URI.create("Ignored")));
  }

  @Test
  void getProfileById() {
    var userProfileResponse = new UserProfileResponse(2L, "Email", "First Name", "Last Name", null);
    given(mockUserService.profileById(anyLong())).willReturn(userProfileResponse);

    assertThat(userController.profileById(9999L, mockRequest))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(userProfileResponse, "Ignored", "Ignored", URI.create("Ignored")));
  }

  @Test
  void postUsers() {
    var userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email@Address",
            "First Name",
            "Last Name",
            Collections.singleton(Role.ROLE_ADMIN.name()));
    var userCreateResponse = "Created user: " + userCreateRequest.getUsername();

    assertThat(userController.signUp(userCreateRequest, mockRequest).getStatusCode())
        .isEqualTo(HttpStatus.CREATED);

    assertThat(userController.signUp(userCreateRequest, mockRequest).getBody())
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(new Response<>(userCreateResponse, "Ignored", "Ignored", URI.create("Ignored")));
  }

  @Test
  void patchUsersById() {
    var userProfileResponse = new UserProfileResponse(2L, "Email", "First Name", "Last Name", null);
    given(mockUserService.profilePatch(anyLong(), any())).willReturn(userProfileResponse);
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("Email", "email@email.net");

    assertThat(userController.profilePatch(999L, patchRequest, mockRequest))
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(
            new Response<>(userProfileResponse, "Ignored", "Ignored", URI.create("Ignored")));
  }

  @Test
  void deleteUserById() {
    String requestParameter = "1";
    var userDeleteResponse = "Deleted user id: " + requestParameter;

    assertThat(userController.deleteById(1L, mockRequest).getStatusCode())
        .isEqualTo(HttpStatus.NO_CONTENT);

    assertThat(userController.deleteById(1L, mockRequest).getBody())
        .usingRecursiveComparison()
        .comparingOnlyFields("size", "content")
        .isEqualTo(new Response<>(userDeleteResponse, "Ignored", "Ignored", URI.create("Ignored")));
  }
}
