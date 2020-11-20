package net.flyingfishflash.ledger.foundation.users.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

@Service
@Transactional
public class UserService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final TenantService tenantService;

  private final PasswordEncoder encoder;

  private final UserProfileMapper userProfileMapper;

  public UserService(
      UserRepository userRepository,
      TenantService tenantService,
      PasswordEncoder encoder,
      UserProfileMapper userProfileMapper) {
    this.userRepository = userRepository;
    this.tenantService = tenantService;
    this.encoder = encoder;
    this.userProfileMapper = userProfileMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);

    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UsernameNotFoundException("Username [" + username + "] not found");
    }
  }

  public Boolean existsById(Long id) {
    return userRepository.existsById(id);
  }

  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public void createUser(UserCreateRequest userSignUpRequest) {

    if (userRepository.existsByUsername(userSignUpRequest.getUsername())) {
      throw new UserCreateException(
          "Username " + userSignUpRequest.getUsername() + " is already in use.");
    }

    if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
      throw new UserCreateException(
          "Email " + userSignUpRequest.getEmail() + " is already in use.");
    }

    // TODO: should be replaced by form validation
    if (userSignUpRequest.getRoles() == null || userSignUpRequest.getRoles().size() < 1) {
      throw new UserCreateException("At least one role must be assigned to a user.");
    }

    // TODO: should be replaced by form validation
    if (userSignUpRequest.getRoles().size() > 1) {
      throw new UserCreateException("Only a single role may be assigned to a user.");
    }

    User user =
        new User(
            userSignUpRequest.getUsername(),
            encoder.encode(userSignUpRequest.getPassword()),
            userSignUpRequest.getEmail(),
            userSignUpRequest.getFirstName(),
            userSignUpRequest.getLastName());

    Set<String> strRoles = userSignUpRequest.getRoles();

    strRoles.forEach(
        role -> {
          switch (role) {
            case "ROLE_ADMIN" -> user.grantAuthority(Role.ROLE_ADMIN);
            case "ROLE_EDITOR" -> user.grantAuthority(Role.ROLE_EDITOR);
            default -> user.grantAuthority(Role.ROLE_VIEWER);
          }
        });

    userRepository.save(user);

    tenantService.initDatabase(user.getUsername());
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User findById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
  }

  public User findByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  public List<User> findAllUsers() {
    return (List<User>) userRepository.findAll();
  }

  public void deleteById(Long userId) {
    try {
      userRepository.deleteById(userId);
    } catch (IllegalArgumentException exception) {
      throw new UserNotFoundException(userId, exception);
    }
    // TODO: also delete the users database schema
  }

  public UserProfileResponse profileById(Long userId) {

    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    return userProfileMapper.mapEntityModelToResponseModel(user);
  }

  public UserProfileResponse profileByUsername(String username) {

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));

    return userProfileMapper.mapEntityModelToResponseModel(user);
  }

  public UserProfileResponse profilePatch(Long userId, Map<String, Object> patchRequest) {

    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    UserProfileRequest userProfileRequest = userProfileMapper.mapEntityModelToRequestModel(user);

    if (patchRequest.size() > 0) {
      for (Entry<String, Object> entry : patchRequest.entrySet()) {
        String change = entry.getKey();
        Object value = entry.getValue();
        switch (change) {
          case "email" -> userProfileRequest.setEmail((String) value);
          case "firstName" -> userProfileRequest.setFirstName((String) value);
          case "lastName" -> userProfileRequest.setLastName((String) value);
          case "password" -> userProfileRequest.setPassword(encoder.encode((String) value));
        }
      }
    }

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    Set<ConstraintViolation<UserProfileRequest>> violations =
        validator.validate(userProfileRequest);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    userProfileMapper.mapRequestModelToEntityModel(userProfileRequest, user);

    // TODO: find out what exactly is saving the new password here

    return userProfileMapper.mapEntityModelToResponseModel(user);
  }
}
