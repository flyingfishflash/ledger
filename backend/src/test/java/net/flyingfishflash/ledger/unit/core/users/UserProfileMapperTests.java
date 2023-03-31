package net.flyingfishflash.ledger.unit.core.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.core.users.data.User;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileMapper;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileRequest;
import net.flyingfishflash.ledger.core.users.data.dto.UserProfileResponse;

/** Unit tests for {@link net.flyingfishflash.ledger.core.users.data.dto.UserProfileMapper} */
@DisplayName("UserProfileMapper")
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
    assertThat(userProfileResponse.password()).isNull();
  }

  @Test
  void mapRequestModelToEntityModel() {
    UserProfileRequest userProfileRequest =
        new UserProfileRequest("Email", "First Name", "Last Name", "Password");
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
    UserProfileRequest userProfileRequest =
        new UserProfileRequest("Email", "First Name", "Last Name", "Password");
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
    assertThat(userProfileResponse.password()).isNull();
  }
}
