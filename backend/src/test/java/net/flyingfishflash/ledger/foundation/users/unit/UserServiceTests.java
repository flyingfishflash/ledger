package net.flyingfishflash.ledger.foundation.users.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import net.flyingfishflash.ledger.foundation.multitenancy.TenantService;
import net.flyingfishflash.ledger.foundation.users.data.Role;
import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.UserRepository;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserCreateRequest;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileMapper;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileRequest;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserCreateException;
import net.flyingfishflash.ledger.foundation.users.exceptions.UserNotFoundException;
import net.flyingfishflash.ledger.foundation.users.service.UserService;

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
    given(mockUserRepository.existsByUsername(username)).willReturn(true);
    userService.existsByUsername(username);
    verify(mockUserRepository, times(1)).existsByUsername(username);
  }

  @Test
  void existsById() {
    var id = 1L;
    given(mockUserRepository.existsById(id)).willReturn(true);
    userService.existsById(id);
    verify(mockUserRepository, times(1)).existsById(id);
  }

  @Test
  void existsByEmail() {
    var email = "Lorem Ipsem";
    given(mockUserRepository.existsByEmail(email)).willReturn(true);
    userService.existsByEmail(email);
    verify(mockUserRepository, times(1)).existsByEmail(email);
  }

  @Test
  void saveUser() {
    given(mockUserRepository.save(any(User.class))).willReturn(new User());
    userService.saveUser(new User());
    verify(mockUserRepository, times(1)).save(any(User.class));
  }

  @Test
  void loadUserByUsername() {
    var u = new User();
    u.setUsername("Lorem Ipsum");
    Optional<User> ou = Optional.of(u);
    given(mockUserRepository.findByUsername(u.getUsername())).willReturn(ou);
    userService.loadUserByUsername(u.getUsername());
    verify(mockUserRepository, times(1)).findByUsername(u.getUsername());
  }

  @Test
  void loadUserByUsername_whenUserNotFound_thenUserNotFoundException() {
    String username = "Lorem Ipsum";
    given(mockUserRepository.findByUsername(username)).willReturn(Optional.empty());
    assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> userService.loadUserByUsername(username));
    verify(mockUserRepository, times(1)).findByUsername(anyString());
  }

  @Test
  void findByUsername() {
    User u = new User();
    u.setUsername("Lorem Ipsum");
    Optional<User> ou = Optional.of(u);
    given(mockUserRepository.findByUsername(u.getUsername())).willReturn(ou);
    userService.findByUsername(u.getUsername());
    verify(mockUserRepository, times(1)).findByUsername(u.getUsername());
  }

  @Test
  void findByUsername_whenUserNotFound_thenUserNotFoundException() {
    var username = "Lorem Ipsum";
    given(mockUserRepository.findByUsername(username)).willReturn(Optional.empty());
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.findByUsername(username));
    verify(mockUserRepository, times(1)).findByUsername(username);
  }

  @Test
  void findById() {
    User u = new User();
    Optional<User> ou = Optional.of(u);
    given(mockUserRepository.findById(anyLong())).willReturn(ou);
    userService.findById(1L);
    verify(mockUserRepository, times(1)).findById(1L);
  }

  @Test
  void findById_whenUserNotFound_thenUserNotFoundException() {
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
  void deleteById_whenUserNotFound_thenUserNotFoundException() {
    var userId = 1L;
    doThrow(IllegalArgumentException.class).when(mockUserRepository).deleteById(anyLong());
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.deleteById(userId))
        .withCause(new IllegalArgumentException());
    verify(mockUserRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void createUser_whenUsernameExists_thenUserCreateException() {
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
  void createUser_whenEmailExists_thenUserCreateException() {
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
  void createUser_whenRolesIsNull_thenUserCreateException() {
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
  void createUser_whenRolesExceedsOne_thenUserCreateException() {
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
  void createUser_whenRolesEmpty_thenUserCreateException() {
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
  void createUser_verifyRoles(Role role) {
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
  void createUser_whenInvalidRoleSpecified_thenUserCreateException() {
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
  }

  @Test
  void profilePatch_whenPatchedIsFalse() {
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
  void profilePatch_whenPatchedIsTrue() {
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
  void profilePatch_whenPatchRequestContainsUnknownKey_thenIllegalStateException() {
    given(mockUserRepository.findById(1L)).willReturn(Optional.of(new User()));
    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("unknownKey", "");
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> userService.profilePatch(1L, patchRequest));
  }

  @Test
  void profilePatch_whenPatchRequestIsEmpty_thenIllegalStateException() {
    given(mockUserRepository.findById(1L)).willReturn(Optional.of(new User()));
    Map<String, Object> patchRequest = new HashMap<>();
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> userService.profilePatch(1L, patchRequest))
        .withMessage("User profile patch request is empty");
  }
}
