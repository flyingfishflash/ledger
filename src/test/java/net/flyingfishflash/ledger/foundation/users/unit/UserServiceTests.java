package net.flyingfishflash.ledger.foundation.users.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolationException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  @InjectMocks private UserService userService;

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder encoder;

  @Mock private TenantService tenantService;

  @Mock private UserProfileMapper userProfileMapper;

  @Test
  public void testExistsByUsername() {
    when(userRepository.existsByUsername(anyString())).thenReturn(true);
    userService.existsByUsername("Any User Name");
    verify(userRepository, times(1)).existsByUsername(anyString());
  }

  @Test
  public void testExistsById() {
    when(userRepository.existsById(anyLong())).thenReturn(true);
    userService.existsById(1L);
    verify(userRepository, times(1)).existsById(anyLong());
  }

  @Test
  public void testExistsByEmail() {
    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    userService.existsByEmail("Any Email Address");
    verify(userRepository, times(1)).existsByEmail(anyString());
  }

  @Test
  public void testSave() {
    when(userRepository.save(any(User.class))).thenReturn(new User());
    userService.saveUser(new User());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  public void testLoadUserByUserName() {
    User u = new User();
    Optional<User> ou = Optional.of(u);
    when(userRepository.findByUsername(anyString())).thenReturn(ou);
    userService.loadUserByUsername("Any User Name");
    verify(userRepository, times(1)).findByUsername(anyString());
  }

  @Test
  public void testLoadUserByUserName_EmptyUser() {
    String username = "Any User Name";
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    assertThrows(
        UsernameNotFoundException.class,
        () -> {
          userService.loadUserByUsername(username);
        });
    verify(userRepository, times(1)).findByUsername(anyString());
  }

  @Test
  public void testFindByUserName() {
    User u = new User();
    Optional<User> ou = Optional.of(u);
    when(userRepository.findByUsername(anyString())).thenReturn(ou);
    userService.findByUsername("Any User Name");
    verify(userRepository, times(1)).findByUsername(anyString());
  }

  @Test
  public void testFindByUserName_EmptyUser() {
    String username = "Any User Name";
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.findByUsername(username);
        });
    verify(userRepository, times(1)).findByUsername(anyString());
  }

  @Test
  public void testFindById() {
    User u = new User();
    Optional<User> ou = Optional.of(u);
    when(userRepository.findById(anyLong())).thenReturn(ou);
    userService.findById(1L);
    verify(userRepository, times(1)).findById(anyLong());
  }

  @Test
  public void testFindById_EmptyUser() {
    Long userId = 1L;
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.findById(userId);
        });
    verify(userRepository, times(1)).findById(anyLong());
  }

  @Test
  public void testFindAllUsers() {
    List<User> userList = new ArrayList<>(1);
    userList.add(new User());
    when(userRepository.findAll()).thenReturn(userList);
    userService.findAllUsers();
    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void testDeleteUser() {
    doNothing().when(userRepository).deleteById(anyLong());
    userService.deleteById(1L);
    verify(userRepository, times(1)).deleteById(anyLong());
  }

  @Test
  public void testDeleteUser_NonExistentId() {
    Long userId = 1L;
    doThrow(IllegalArgumentException.class).when(userRepository).deleteById(anyLong());
    assertThrows(
        UserNotFoundException.class,
        () -> {
          userService.deleteById(userId);
        });
    verify(userRepository, times(1)).deleteById(anyLong());
  }

  @Test
  public void testCreateUser_UsernameExists() {
    when(userRepository.existsByUsername(anyString())).thenReturn(true);
    assertThrows(
        UserCreateException.class,
        () -> {
          UserCreateRequest userCreateRequest = new UserCreateRequest();
          userCreateRequest.setUsername("Any Username");
          userService.createUser(userCreateRequest);
        });
    verify(userRepository, times(1)).existsByUsername(anyString());
    verify(userRepository, times(0)).save(any(User.class));
    verify(tenantService, times(0)).initDatabase(anyString());
  }

  @Test
  public void testCreateUser_EmailExists() {
    when(userRepository.existsByEmail(anyString())).thenReturn(true);
    assertThrows(
        UserCreateException.class,
        () -> {
          UserCreateRequest userCreateRequest = new UserCreateRequest();
          userCreateRequest.setEmail("Any Email Address");
          userService.createUser(userCreateRequest);
        });
    verify(userRepository, times(1)).existsByEmail(anyString());
    verify(userRepository, times(0)).save(any(User.class));
    verify(tenantService, times(0)).initDatabase(anyString());
  }

  @Test
  public void testCreateUser_RolesIsNull() {
    assertThrows(
        UserCreateException.class,
        () -> {
          UserCreateRequest userCreateRequest = new UserCreateRequest();
          userCreateRequest.setUsername("Username");
          userCreateRequest.setEmail("Email Address");
          userService.createUser(userCreateRequest);
        });
    verify(userRepository, times(1)).existsByUsername(anyString());
    verify(userRepository, times(1)).existsByEmail(anyString());
    verify(userRepository, times(0)).save(any(User.class));
    verify(tenantService, times(0)).initDatabase(anyString());
  }

  @Test
  public void testCreateUser_RolesGreaterThanOne() {
    assertThrows(
        UserCreateException.class,
        () -> {
          UserCreateRequest userCreateRequest = new UserCreateRequest();
          userCreateRequest.setUsername("Username");
          userCreateRequest.setEmail("Email Address");
          Set<String> roles = new HashSet<>(2);
          roles.add("Role1");
          roles.add("Role2");
          userCreateRequest.setRoles(roles);
          userService.createUser(userCreateRequest);
        });
    verify(userRepository, times(1)).existsByUsername(anyString());
    verify(userRepository, times(1)).existsByEmail(anyString());
    verify(userRepository, times(0)).save(any(User.class));
    verify(tenantService, times(0)).initDatabase(anyString());
  }

  @Test
  public void testCreateUser_RolesEmpty() {
    assertThrows(
        UserCreateException.class,
        () -> {
          UserCreateRequest userCreateRequest = new UserCreateRequest();
          userCreateRequest.setUsername("Username");
          userCreateRequest.setEmail("Email Address");
          userCreateRequest.setRoles(new HashSet<String>(2));
          userService.createUser(userCreateRequest);
        });
    verify(userRepository, times(1)).existsByUsername(anyString());
    verify(userRepository, times(1)).existsByEmail(anyString());
    verify(userRepository, times(0)).save(any(User.class));
    verify(tenantService, times(0)).initDatabase(anyString());
  }

  @Test
  public void testCreateUser_DefaultRole() {

    UserCreateRequest userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email Address",
            "First Name",
            "Last Name",
            Collections.singleton("Role"));

    when(userRepository.save(any(User.class))).thenReturn(new User());
    when(encoder.encode(anyString())).thenReturn("Any Encoded Password");
    doNothing().when(tenantService).initDatabase(anyString());

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    userService.createUser(userCreateRequest);

    verify(encoder, times(1)).encode(anyString());
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    verify(tenantService, times(1)).initDatabase(anyString());

    assertEquals(1, userArgumentCaptor.getValue().getAuthorities().size());
    assertEquals(
        Role.ROLE_VIEWER.name(), userArgumentCaptor.getValue().getAuthorities().get(0).toString());
  }

  @Test
  public void testCreateUser_EditorRole() {
    UserCreateRequest userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email Address",
            "First Name",
            "Last Name",
            Collections.singleton(Role.ROLE_EDITOR.name()));

    when(userRepository.save(any(User.class))).thenReturn(new User());
    when(encoder.encode(anyString())).thenReturn("Any Encoded Password");
    doNothing().when(tenantService).initDatabase(anyString());

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    userService.createUser(userCreateRequest);

    verify(encoder, times(1)).encode(anyString());
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    verify(tenantService, times(1)).initDatabase(anyString());

    assertEquals(1, userArgumentCaptor.getValue().getAuthorities().size());
    assertEquals(
        Role.ROLE_EDITOR.name(), userArgumentCaptor.getValue().getAuthorities().get(0).toString());
  }

  @Test
  public void testCreateUser_AdminRole() {
    UserCreateRequest userCreateRequest =
        new UserCreateRequest(
            "Username",
            "Password",
            "Email Address",
            "First Name",
            "Last Name",
            Collections.singleton(Role.ROLE_ADMIN.name()));

    when(userRepository.save(any(User.class))).thenReturn(new User());
    when(encoder.encode(anyString())).thenReturn("Any Encoded Password");
    doNothing().when(tenantService).initDatabase(anyString());

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    userService.createUser(userCreateRequest);

    verify(encoder, times(1)).encode(anyString());
    verify(userRepository, times(1)).save(userArgumentCaptor.capture());
    verify(tenantService, times(1)).initDatabase(anyString());

    assertEquals(1, userArgumentCaptor.getValue().getAuthorities().size());
    assertEquals(
        Role.ROLE_ADMIN.name(), userArgumentCaptor.getValue().getAuthorities().get(0).toString());
  }

  @Test
  public void testProfileById() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    when(userProfileMapper.mapEntityModelToResponseModel(any(User.class)))
        .thenReturn(new UserProfileResponse());
    UserProfileResponse userProfileResponse = userService.profileById(1L);
    verify(userRepository, times(1)).findById(anyLong());
    verify(userProfileMapper, times(1)).mapEntityModelToResponseModel(any(User.class));
    assertNotNull(userProfileResponse);
  }

  @Test
  public void testProfileByUsername() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
    when(userProfileMapper.mapEntityModelToResponseModel(any(User.class)))
        .thenReturn(new UserProfileResponse());
    UserProfileResponse userProfileResponse = userService.profileByUsername(anyString());
    verify(userRepository, times(1)).findByUsername(anyString());
    verify(userProfileMapper, times(1)).mapEntityModelToResponseModel(any(User.class));
    assertNotNull(userProfileResponse);
  }

  @Test
  public void testProfilePatch() {
    when(userProfileMapper.mapEntityModelToRequestModel(any(User.class)))
        .thenReturn(new UserProfileRequest());

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    when(encoder.encode(anyString())).thenReturn("Any Encoded Password");

    Map<String, Object> patchRequest = new HashMap<>();
    patchRequest.put("email", "email@email.em");
    patchRequest.put("firstName", "First Name");
    patchRequest.put("lastName", "Last Name");
    patchRequest.put("password", "Password");

    userService.profilePatch(1L, patchRequest);

    verify(userRepository, times(1)).findById(anyLong());
    verify(userProfileMapper, times(1)).mapEntityModelToRequestModel(any(User.class));
  }

  @Test
  public void testProfilePatch_ConstraintViolationException() {
    when(userProfileMapper.mapEntityModelToRequestModel(any(User.class)))
        .thenReturn(new UserProfileRequest());

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    //    when(encoder.encode(anyString())).thenReturn("Any Encoded Password");

    assertThrows(
        ConstraintViolationException.class,
        () -> {
          Map<String, Object> patchRequest = new HashMap<>();
          UserCreateRequest userCreateRequest = new UserCreateRequest();
          userCreateRequest.setUsername("Username");
          userCreateRequest.setEmail("Invalid Email Address");
          userCreateRequest.setRoles(new HashSet<String>(2));
          userService.profilePatch(1L, patchRequest);
        });

    verify(userRepository, times(1)).findById(anyLong());
    verify(userProfileMapper, times(1)).mapEntityModelToRequestModel(any(User.class));
    verify(userProfileMapper, times(0))
        .mapRequestModelToEntityModel(any(UserProfileRequest.class), any(User.class));
    verify(userProfileMapper, times(0)).mapEntityModelToResponseModel(any(User.class));
  }
}
