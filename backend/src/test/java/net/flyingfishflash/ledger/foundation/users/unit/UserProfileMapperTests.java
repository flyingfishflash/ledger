package net.flyingfishflash.ledger.foundation.users.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileMapper;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileRequest;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileResponse;

class UserProfileMapperTests {

  private final UserProfileMapper userProfileMapper = new UserProfileMapper();

  @Test
  void testMapEntityModelToRequestModel() {

    User user = new User("Username", "Password", "Email", "First Name", "Last Name");

    UserProfileRequest userProfileRequest = userProfileMapper.mapEntityModelToRequestModel(user);

    assertEquals(user.getPassword(), userProfileRequest.getPassword());
    assertEquals(user.getEmail(), userProfileRequest.getEmail());
    assertEquals(user.getFirstName(), userProfileRequest.getFirstName());
    assertEquals(user.getLastName(), userProfileRequest.getLastName());
  }

  @Test
  void testMapEntityModelToResponseModel() {

    User user = new User("Username", "Password", "Email", "First Name", "Last Name");

    UserProfileResponse userProfileResponse = userProfileMapper.mapEntityModelToResponseModel(user);

    assertEquals("", userProfileResponse.getPassword());
    assertEquals(user.getEmail(), userProfileResponse.getEmail());
    assertEquals(user.getFirstName(), userProfileResponse.getFirstName());
    assertEquals(user.getLastName(), userProfileResponse.getLastName());
  }

  @Test
  void testMapRequestModelToEntityModel() {

    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("Email");
    userProfileRequest.setFirstName("First Name");
    userProfileRequest.setLastName("Last Name");
    userProfileRequest.setPassword("Password");

    User user = new User();

    userProfileMapper.mapRequestModelToEntityModel(userProfileRequest, user);
    assertEquals(user.getPassword(), userProfileRequest.getPassword());
    assertEquals(user.getEmail(), userProfileRequest.getEmail());
    assertEquals(user.getFirstName(), userProfileRequest.getFirstName());
    assertEquals(user.getLastName(), userProfileRequest.getLastName());
  }

  @Test
  void testMapRequestModelToResponseModel() {

    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("Email");
    userProfileRequest.setFirstName("First Name");
    userProfileRequest.setLastName("Last Name");
    userProfileRequest.setPassword("Password");

    UserProfileResponse userProfileResponse =
        userProfileMapper.mapRequestModelToResponseModel(userProfileRequest);
    assertEquals("", userProfileResponse.getPassword());
    assertEquals(userProfileRequest.getEmail(), userProfileResponse.getEmail());
    assertEquals(userProfileRequest.getFirstName(), userProfileResponse.getFirstName());
    assertEquals(userProfileRequest.getLastName(), userProfileResponse.getLastName());
  }
}
