package net.flyingfishflash.ledger.unit.core.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import net.flyingfishflash.ledger.core.users.data.Role;
import net.flyingfishflash.ledger.core.users.data.User;
import net.flyingfishflash.ledger.core.users.data.dto.UserCreateRequest;
import net.flyingfishflash.ledger.core.users.data.dto.UserCreateResponse;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.core.users.service.UserService;
import net.flyingfishflash.ledger.core.users.web.UserController;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

  private MockMvc mvc;

  @Mock private UserService mockUserService;
  @Mock private Principal mockPrincipal;
  @InjectMocks UserController userController;

  private JacksonTester<UserProfileResponse> jsonUserProfileResponse;
  private JacksonTester<UserCreateRequest> jsonUserCreateRequest;
  private JacksonTester<UserCreateResponse> jsonUserCreateResponse;
  //  private JacksonTester<UserDeleteResponse> jsonUserDeleteResponse;
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
  void getUsers() throws Exception {
    User user = new User("Username", "Password", "Email@Email", "First Name", "Last Name");
    user.grantAuthority(Role.ROLE_VIEWER);
    List<User> userList = new ArrayList<>(1);
    userList.add(user);
    given(mockUserService.findAllUsers()).willReturn(userList);
    assertThat(
            mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonUserList.write(userList).getJson());
  }

  @Test
  void getProfileByUsername() throws Exception {
    var userProfileResponse = new UserProfileResponse(2L, "Email", "First Name", "Last Name", null);
    given(mockPrincipal.getName()).willReturn("Any Principal");
    given(mockUserService.profileByUsername(anyString())).willReturn(userProfileResponse);
    assertThat(
            mvc.perform(get("/users/profile").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonUserProfileResponse.write(userProfileResponse).getJson());
  }

  @Test
  void getProfileById() throws Exception {
    given(mockUserService.profileById(anyLong()))
        .willReturn(new UserProfileResponse(2L, "Email", "First Name", "Last Name", null));
    assertThat(
            mvc.perform(get("/users/2/profile"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(
            jsonUserProfileResponse
                .write(new UserProfileResponse(2L, "Email", "First Name", "Last Name", null))
                .getJson());
  }

  @Test
  void postUsers() throws Exception {
    var userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email@Address",
            "First Name",
            "Last Name",
            Collections.singleton(Role.ROLE_ADMIN.name()));
    var expectedUserCreateResponse =
        new UserCreateResponse("Created user: " + userCreateRequest.getUsername());
    assertThat(
            mvc.perform(
                    post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserCreateRequest.write(userCreateRequest).getJson()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonUserCreateResponse.write(expectedUserCreateResponse).getJson());
  }

  @Test
  void patchUsersById() throws Exception {
    var expectedUserProfileResponse =
        new UserProfileResponse(2L, "Email", "First Name", "Last Name", null);
    given(mockUserService.profilePatch(anyLong(), any())).willReturn(expectedUserProfileResponse);
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("Email", "email@email.net");
    assertThat(
            mvc.perform(
                    patch("/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPatchRequest.write(patchRequest).getJson()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo(jsonUserProfileResponse.write(expectedUserProfileResponse).getJson());
  }

  @Test
  void deleteUserById() throws Exception {
    String requestParameter = "1";
    assertThat(
            mvc.perform(delete("/users/delete?id=" + requestParameter))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString())
        .isEqualTo("{\"message\":\"Deleted user id: " + requestParameter + "\"}");
  }
}
