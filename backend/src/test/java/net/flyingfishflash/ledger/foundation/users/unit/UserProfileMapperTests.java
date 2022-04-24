package net.flyingfishflash.ledger.foundation.users.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileMapper;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileRequest;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileResponse;

class UserProfileMapperTests {

  private final UserProfileMapper userProfileMapper = new UserProfileMapper();

  @Test
  void mapEntityModelToRequestModel() {
    User user = new User("Username", "Password", "Email", "First Name", "Last Name");
    UserProfileRequest userProfileRequest = userProfileMapper.mapEntityModelToRequestModel(user);
    assertThat(user)
        .usingRecursiveComparison()
        .comparingOnlyFields("password", "email", "firstName", "lastName")
        .ignoringFields(
            "id",
            "username",
            "credentialsNonExpired",
            "roles",
            "accountNonExpired",
            "enabled",
            "accountNonLocked")
        .isEqualTo(userProfileRequest);
  }

  @Test
  void mapEntityModelToResponseModel() {
    User user = new User("Username", "Password", "Email", "First Name", "Last Name");
    UserProfileResponse userProfileResponse = userProfileMapper.mapEntityModelToResponseModel(user);
    assertThat(user)
        .usingRecursiveComparison()
        .comparingOnlyFields("email", "firstName", "lastName")
        .ignoringFields(
            "id",
            "password",
            "username",
            "credentialsNonExpired",
            "roles",
            "accountNonExpired",
            "enabled",
            "accountNonLocked")
        .isEqualTo(userProfileResponse);
    assertThat(userProfileResponse.getPassword()).isEmpty();
  }

  @Test
  void mapRequestModelToEntityModel() {
    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("Email");
    userProfileRequest.setFirstName("First Name");
    userProfileRequest.setLastName("Last Name");
    userProfileRequest.setPassword("Password");
    User user = new User();
    userProfileMapper.mapRequestModelToEntityModel(userProfileRequest, user);
    assertThat(user)
        .usingRecursiveComparison()
        .comparingOnlyFields("email", "firstName", "lastName", "password")
        .ignoringFields(
            "id",
            "username",
            "credentialsNonExpired",
            "roles",
            "accountNonExpired",
            "enabled",
            "accountNonLocked")
        .isEqualTo(userProfileRequest);
  }

  @Test
  void mapRequestModelToResponseModel() {
    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("Email");
    userProfileRequest.setFirstName("First Name");
    userProfileRequest.setLastName("Last Name");
    userProfileRequest.setPassword("Password");
    UserProfileResponse userProfileResponse =
        userProfileMapper.mapRequestModelToResponseModel(userProfileRequest);
    assertThat(userProfileRequest)
        .usingRecursiveComparison()
        .comparingOnlyFields("email", "firstName", "lastName")
        .ignoringFields(
            "id",
            "password",
            "username",
            "credentialsNonExpired",
            "roles",
            "accountNonExpired",
            "enabled",
            "accountNonLocked")
        .isEqualTo(userProfileResponse);
    assertThat(userProfileResponse.getPassword()).isEmpty();
  }
}
