package net.flyingfishflash.ledger.unit.core.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.ConstraintViolationException;

import net.flyingfishflash.ledger.core.multitenancy.TenantService;
import net.flyingfishflash.ledger.core.users.data.Role;
import net.flyingfishflash.ledger.core.users.data.User;
import net.flyingfishflash.ledger.core.users.data.UserRepository;
import net.flyingfishflash.ledger.core.users.data.dto.UserCreateRequest;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileMapper;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileRequest;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.core.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.core.users.exceptions.UserNotFoundException;
import net.flyingfishflash.ledger.core.users.service.UserService;

/** Unit tests for {@link net.flyingfishflash.ledger.core.users.service.UserService} */
@DisplayName("UserService")
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

  @Mock private UserRepository mockUserRepository;
  @Mock private PasswordEncoder mockEncoder;
  @Mock private TenantService mockTenantService;
  @Mock private UserProfileMapper mockUserProfileMapper;
  @InjectMocks private UserService userService;

  @Test
  void existsByUsername() {
    var username = "Lorem Ipsum";
    var existsByUsername = false;
    given(mockUserRepository.existsByUsername(anyString())).willReturn(true);
    existsByUsername = userService.existsByUsername(username);
    verify(mockUserRepository, times(1)).existsByUsername(anyString());
    assertThat(existsByUsername).isTrue();
    reset(mockUserRepository);
    given(mockUserRepository.existsByUsername(anyString())).willReturn(false);
    existsByUsername = userService.existsByUsername(username);
    verify(mockUserRepository, times(1)).existsByUsername(anyString());
    assertThat(existsByUsername).isFalse();
  }

  @Test
  void existsById() {
    var id = 1L;
    var existsById = false;
    given(mockUserRepository.existsById(anyLong())).willReturn(true);
    existsById = userService.existsById(id);
    verify(mockUserRepository, times(1)).existsById(anyLong());
    assertThat(existsById).isTrue();
    reset(mockUserRepository);
    given(mockUserRepository.existsById(anyLong())).willReturn(false);
    existsById = userService.existsById(id);
    verify(mockUserRepository, times(1)).existsById(anyLong());
    assertThat(existsById).isFalse();
  }

  @Test
  void existsByEmail() {
    var email = "Lorem Ipsem";
    var existsByEmail = false;
    given(mockUserRepository.existsByEmail(anyString())).willReturn(true);
    existsByEmail = userService.existsByEmail(email);
    verify(mockUserRepository, times(1)).existsByEmail(anyString());
    assertThat(existsByEmail).isTrue();
    reset(mockUserRepository);
    given(mockUserRepository.existsByEmail(anyString())).willReturn(false);
    existsByEmail = userService.existsByEmail(email);
    verify(mockUserRepository, times(1)).existsByEmail(anyString());
    assertThat(existsByEmail).isFalse();
  }

  @Test
  void saveUser() {
    given(mockUserRepository.save(any(User.class))).willReturn(new User());
    userService.saveUser(new User());
    verify(mockUserRepository, times(1)).save(any(User.class));
    assertThat(userService.saveUser(new User())).isEqualTo(new User());
  }

  @Test
  void loadUserByUsername() {
    var u = new User();
    u.setUsername("Lorem Ipsum");
    Optional<User> ou = Optional.of(u);
    given(mockUserRepository.findByUsername(u.getUsername())).willReturn(ou);
    var loadedUser = userService.loadUserByUsername(u.getUsername());
    verify(mockUserRepository, times(1)).findByUsername(u.getUsername());
    assertThat(loadedUser).isEqualTo(u);
  }

  @Test
  void loadUserByUsernameWhenUserNotFoundThenUserNotFoundException() {
    String username = "Lorem Ipsum";
    given(mockUserRepository.findByUsername(username)).willReturn(Optional.empty());
    assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> userService.loadUserByUsername(username));
    verify(mockUserRepository, times(1)).findByUsername(anyString());
  }

  @Test
  void findByUsername() {
    var u = new User();
    u.setUsername("Lorem Ipsum");
    Optional<User> ou = Optional.of(u);
    given(mockUserRepository.findByUsername(u.getUsername())).willReturn(ou);
    var foundUser = userService.findByUsername(u.getUsername());
    verify(mockUserRepository, times(1)).findByUsername(u.getUsername());
    assertThat(foundUser).isEqualTo(u);
  }

  @Test
  void findByUsernameWhenUserNotFoundThenUserNotFoundException() {
    var username = "Lorem Ipsum";
    given(mockUserRepository.findByUsername(username)).willReturn(Optional.empty());
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.findByUsername(username));
    verify(mockUserRepository, times(1)).findByUsername(username);
  }

  @Test
  void findById() {
    var u = new User();
    Optional<User> ou = Optional.of(u);
    given(mockUserRepository.findById(anyLong())).willReturn(ou);
    var foundUser = userService.findById(1L);
    verify(mockUserRepository, times(1)).findById(1L);
    assertThat(foundUser).isEqualTo(u);
  }

  @Test
  void findByIdWhenUserNotFoundThenUserNotFoundException() {
    var userId = 1L;
    given(mockUserRepository.findById(userId)).willReturn(Optional.empty());
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.findById(userId));
    verify(mockUserRepository, times(1)).findById(userId);
  }

  @Test
  void findAllUsers() {
    List<User> userList = new ArrayList<>(1);
    userList.add(new User());
    given(mockUserRepository.findAll()).willReturn(userList);
    userService.findAllUsers();
    verify(mockUserRepository, times(1)).findAll();
  }

  @Test
  void deleteById() {
    doNothing().when(mockUserRepository).deleteById(anyLong());
    userService.deleteById(1L);
    verify(mockUserRepository, times(1)).deleteById(1L);
  }

  @Test
  void deleteByIdWhenUserNotFoundThenUserNotFoundException() {
    var userId = 1L;
    doThrow(IllegalArgumentException.class).when(mockUserRepository).deleteById(anyLong());
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.deleteById(userId))
        .withCause(new IllegalArgumentException());
    verify(mockUserRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void createUserWhenUsernameExistsThenUserCreateException() {
    var userCreateRequest = new UserCreateRequest();
    userCreateRequest.setUsername("Lorem Ipsum");
    given(mockUserRepository.existsByUsername(userCreateRequest.getUsername())).willReturn(true);
    assertThatExceptionOfType(UserCreateException.class)
        .isThrownBy(() -> userService.createUser(userCreateRequest));
    verify(mockUserRepository, times(1)).existsByUsername(userCreateRequest.getUsername());
    verify(mockUserRepository, times(0)).save(any(User.class));
    verify(mockTenantService, times(0)).initDatabase(anyString());
  }

  @Test
  void createUserWhenEmailExistsThenUserCreateException() {
    var userCreateRequest = new UserCreateRequest();
    userCreateRequest.setEmail("Lorem Ipsum");
    given(mockUserRepository.existsByEmail(userCreateRequest.getEmail())).willReturn(true);
    assertThatExceptionOfType(UserCreateException.class)
        .isThrownBy(() -> userService.createUser(userCreateRequest));
    verify(mockUserRepository, times(1)).existsByEmail(userCreateRequest.getEmail());
    verify(mockUserRepository, times(0)).save(any(User.class));
    verify(mockTenantService, times(0)).initDatabase(anyString());
  }

  @Test
  void createUserWhenRolesIsNullThenUserCreateException() {
    var userCreateRequest = new UserCreateRequest();
    userCreateRequest.setUsername("Username");
    userCreateRequest.setEmail("Email Address");
    assertThatExceptionOfType(UserCreateException.class)
        .isThrownBy(() -> userService.createUser(userCreateRequest));
    verify(mockUserRepository, times(1)).existsByUsername(userCreateRequest.getUsername());
    verify(mockUserRepository, times(1)).existsByEmail(userCreateRequest.getEmail());
    verify(mockUserRepository, times(0)).save(any(User.class));
    verify(mockTenantService, times(0)).initDatabase(anyString());
  }

  @Test
  void createUserWhenRolesExceedsOneThenUserCreateException() {
    var userCreateRequest = new UserCreateRequest();
    userCreateRequest.setUsername("Username");
    userCreateRequest.setEmail("Email Address");
    Set<String> roles = new HashSet<>(2);
    roles.add("Role1");
    roles.add("Role2");
    userCreateRequest.setRoles(roles);
    assertThatExceptionOfType(UserCreateException.class)
        .isThrownBy(() -> userService.createUser(userCreateRequest));
    verify(mockUserRepository, times(1)).existsByUsername(userCreateRequest.getUsername());
    verify(mockUserRepository, times(1)).existsByEmail(userCreateRequest.getEmail());
    verify(mockUserRepository, times(0)).save(any(User.class));
    verify(mockTenantService, times(0)).initDatabase(anyString());
  }

  @Test
  void createUserWhenRolesEmptyThenUserCreateException() {
    var userCreateRequest = new UserCreateRequest();
    userCreateRequest.setUsername("Username");
    userCreateRequest.setEmail("Email Address");
    userCreateRequest.setRoles(new HashSet<>(2));
    assertThatExceptionOfType(UserCreateException.class)
        .isThrownBy(() -> userService.createUser(userCreateRequest));
    verify(mockUserRepository, times(1)).existsByUsername(userCreateRequest.getUsername());
    verify(mockUserRepository, times(1)).existsByEmail(userCreateRequest.getEmail());
    verify(mockUserRepository, times(0)).save(any(User.class));
    verify(mockTenantService, times(0)).initDatabase(anyString());
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  void createUserVerifyRoles(Role role) {
    UserCreateRequest userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email Address",
            "First Name",
            "Last Name",
            Collections.singleton(role.name()));
    given(mockUserRepository.save(any(User.class))).willReturn(new User());
    given(mockEncoder.encode(anyString())).willReturn("Any Encoded Password");
    doNothing().when(mockTenantService).initDatabase(anyString());
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    userService.createUser(userCreateRequest);
    verify(mockEncoder, times(1)).encode(anyString());
    verify(mockUserRepository, times(1)).save(userArgumentCaptor.capture());
    verify(mockTenantService, times(1)).initDatabase(anyString());
    assertThat(userArgumentCaptor.getValue().getAuthorities())
        .containsExactly(new SimpleGrantedAuthority(role.name()));
  }

  @Test
  void createUserWhenInvalidRoleSpecifiedThenUserCreateException() {
    UserCreateRequest userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email Address",
            "First Name",
            "Last Name",
            Collections.singleton("Role"));
    assertThatExceptionOfType(UserCreateException.class)
        .isThrownBy(() -> userService.createUser(userCreateRequest));
  }

  @Test
  void profileById() {
    given(mockUserRepository.findById(anyLong())).willReturn(Optional.of(new User()));
    given(mockUserProfileMapper.mapEntityModelToResponseModel(any(User.class)))
        .willReturn(new UserProfileResponse(1L, null, null, null, null));
    var userProfileResponse = userService.profileById(1L);
    verify(mockUserRepository, times(1)).findById(1L);
    verify(mockUserProfileMapper, times(1)).mapEntityModelToResponseModel(any(User.class));
    assertThat(userProfileResponse).isNotNull();
    assertThat(userProfileResponse.id()).isEqualTo(1L);
  }

  @Test
  void profileByUsername() {
    given(mockUserRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
    given(mockUserProfileMapper.mapEntityModelToResponseModel(any(User.class)))
        .willReturn(new UserProfileResponse(1L, null, null, null, null));
    var userProfileResponse = userService.profileByUsername(anyString());
    verify(mockUserRepository, times(1)).findByUsername(anyString());
    verify(mockUserProfileMapper, times(1)).mapEntityModelToResponseModel(any(User.class));
    assertThat(userProfileResponse).isNotNull();
    assertThat(userProfileResponse.id()).isEqualTo(1L);
  }

  @Test
  void profilePatchWhenPatchedIsFalse() {
    var user = new User();
    user.setEmail("email@email.em");
    user.setFirstName("First Name");
    user.setLastName("Last Name");
    user.setPassword("Password");
    given(mockUserRepository.findById(anyLong())).willReturn(Optional.of(user));
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("email", "email@email.em");
    patchRequest.put("firstName", "First Name");
    patchRequest.put("lastName", "Last Name");
    patchRequest.put("password", "Password");
    userService.profilePatch(1L, patchRequest);
    verify(mockUserRepository, times(1)).findById(anyLong());
    verify(mockUserProfileMapper, times(0))
        .mapRequestModelToEntityModel(any(UserProfileRequest.class), any(User.class));
    verify(mockUserProfileMapper, times(1)).mapEntityModelToResponseModel(any(User.class));
  }

  @Test
  void profilePatchWhenPatchedIsTrue() {
    var user = new User();
    user.setEmail("email@email.em");
    user.setFirstName("First Name");
    user.setLastName("Last Name");
    user.setPassword("Password");
    given(mockUserRepository.findById(anyLong())).willReturn(Optional.of(user));
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("email", "email@email.em1");
    patchRequest.put("firstName", "First Name1");
    patchRequest.put("lastName", "Last Name1");
    patchRequest.put("password", "Password1");
    userService.profilePatch(1L, patchRequest);
    verify(mockUserRepository, times(1)).findById(anyLong());
    verify(mockUserProfileMapper, times(1))
        .mapRequestModelToEntityModel(any(UserProfileRequest.class), any(User.class));
    verify(mockUserProfileMapper, times(1)).mapEntityModelToResponseModel(any(User.class));
  }

  @Test
  void profilePatch_whenPatchRequestFailsValidation_thenConstraintViolationException() {
    var user = new User();
    user.setEmail("email@email.em");
    user.setFirstName("First Name");
    user.setLastName("Last Name");
    user.setPassword("Any Password");
    given(mockUserRepository.findById(anyLong())).willReturn(Optional.of(user));
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("email", "");
    patchRequest.put("firstName", "");
    patchRequest.put("lastName", "");
    patchRequest.put("password", "");
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> userService.profilePatch(1L, patchRequest));
    verify(mockUserRepository, times(1)).findById(anyLong());
    verify(mockUserProfileMapper, times(0))
        .mapRequestModelToEntityModel(any(UserProfileRequest.class), any(User.class));
    verify(mockUserProfileMapper, times(0)).mapEntityModelToResponseModel(any(User.class));
  }

  @Test
  void profilePatchWhenPatchRequestContainsUnknownKeyThenIllegalStateException() {
    given(mockUserRepository.findById(1L)).willReturn(Optional.of(new User()));
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("unknownKey", "");
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> userService.profilePatch(1L, patchRequest));
  }

  @Test
  void profilePatchWhenPatchRequestIsEmptyThenIllegalStateException() {
    given(mockUserRepository.findById(1L)).willReturn(Optional.of(new User()));
    Map<String, Object> patchRequest = new HashMap<>();
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> userService.profilePatch(1L, patchRequest))
        .withMessage("User profile patch request is empty");
  }
}
