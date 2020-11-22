package net.flyingfishflash.ledger.foundation.users.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.foundation.users.data.Role;
import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserCreateRequest;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserCreateResponse;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserDeleteResponse;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.foundation.users.service.UserService;
import net.flyingfishflash.ledger.foundation.users.web.UserController;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

  private MockMvc mvc;

  @InjectMocks UserController userController;

  @Mock private UserService userService;

  @Mock private Principal mockPrincipal;

  private JacksonTester<UserProfileResponse> jsonUserProfileResponse;
  private JacksonTester<UserCreateRequest> jsonUserCreateRequest;
  private JacksonTester<UserCreateResponse> jsonUserCreateResponse;
  private JacksonTester<UserDeleteResponse> jsonUserDeleteResponse;
  private JacksonTester<List<User>> jsonUserList;
  private JacksonTester<Map<String, Object>> jsonPatchRequest;

  @BeforeEach
  public void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
    // MockMvc standalone approach
    mvc =
        MockMvcBuilders.standaloneSetup(userController)
            // .setControllerAdvice(new AdviceForUserExceptions())
            // .setControllerAdvice(new AdviceForStandardExceptions())
            // .addFilters(new SuperHeroFilter())
            .build();
  }

  @Test
  public void testFindAllUsers() throws Exception {

    User user = new User("Username", "Password", "Email@Email", "First Name", "Last Name");
    user.grantAuthority(Role.ROLE_VIEWER);
    List<User> userList = new ArrayList<>(1);
    userList.add(user);

    given(userService.findAllUsers()).willReturn(((userList)));

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/users").accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(userService, times(1)).findAllUsers();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo(jsonUserList.write(userList).getJson());
  }

  @Test
  public void profileByUsername() throws Exception {

    UserProfileResponse userProfileResponse =
        new UserProfileResponse(2L, "Email", "First Name", "Last Name");

    given(mockPrincipal.getName()).willReturn("Any Principal");

    given(userService.profileByUsername(anyString())).willReturn(userProfileResponse);

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/users/profile").principal(mockPrincipal))
            .andReturn()
            .getResponse();

    verify(userService, times(1)).profileByUsername(anyString());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    assertThat(response.getContentAsString())
        .isEqualTo(jsonUserProfileResponse.write(userProfileResponse).getJson());
  }

  @Test
  public void testProfileById() throws Exception {

    given(userService.profileById(anyLong()))
        .willReturn(new UserProfileResponse(2L, "Email", "First Name", "Last Name"));

    MockHttpServletResponse response =
        mvc.perform(get("/api/v1/ledger/users/2/profile").accept(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

    verify(userService, times(1)).profileById(anyLong());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(
            jsonUserProfileResponse
                .write(new UserProfileResponse(2L, "Email", "First Name", "Last Name"))
                .getJson());
  }

  @Test
  public void testCreateUser() throws Exception {
    UserCreateRequest userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email@Address",
            "First Name",
            "Last Name",
            Collections.singleton(Role.ROLE_ADMIN.name()));

    UserCreateResponse expectedUserCreateResponse =
        new UserCreateResponse("Created user: " + userCreateRequest.getUsername());

    MockHttpServletResponse response =
        mvc.perform(
                post("/api/v1/ledger/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonUserCreateRequest.write(userCreateRequest).getJson()))
            .andReturn()
            .getResponse();

    verify(userService, times(1)).createUser(any(UserCreateRequest.class));

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonUserCreateResponse.write(expectedUserCreateResponse).getJson());
  }

  @Test
  public void testPatchUser() throws Exception {

    UserProfileResponse expectedUserProfileResponse =
        new UserProfileResponse(2L, "Email", "First Name", "Last Name");

    given(userService.profilePatch(anyLong(), any())).willReturn(expectedUserProfileResponse);

    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("Email", "email@email.net");

    MockHttpServletResponse response =
        mvc.perform(
                patch("/api/v1/ledger/users/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPatchRequest.write(patchRequest).getJson()))
            .andReturn()
            .getResponse();

    verify(userService, times(1)).profilePatch(anyLong(), any());

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString())
        .isEqualTo(jsonUserProfileResponse.write(expectedUserProfileResponse).getJson());
  }

  @Test
  public void testDeleteUser() throws Exception {

    String requestParameter = "1";

    MockHttpServletResponse response =
        mvc.perform(delete("/api/v1/ledger/users/delete?id=" + requestParameter))
            .andReturn()
            .getResponse();

    verify(userService, times(1)).deleteById(anyLong());
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}
