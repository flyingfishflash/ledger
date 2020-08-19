package net.flyingfishflash.ledger.users.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.flyingfishflash.ledger.users.data.User;
import net.flyingfishflash.ledger.users.data.dto.UserProfileMapper;
import net.flyingfishflash.ledger.users.data.dto.UserProfileRequest;
import net.flyingfishflash.ledger.users.data.dto.UserProfileResponse;
import org.junit.jupiter.api.Test;

public class UserProfileMapperTests {

  private final UserProfileMapper userProfileMapper = new UserProfileMapper();

  @Test
  public void testMapEntityModelToRequestModel() {

    User user = new User("Username", "Password", "Email", "First Name", "Last Name");

    UserProfileRequest userProfileRequest = userProfileMapper.mapEntityModelToRequestModel(user);

    assertEquals(user.getPassword(), userProfileRequest.getPassword());
    assertEquals(user.getEmail(), userProfileRequest.getEmail());
    assertEquals(user.getFirstName(), userProfileRequest.getFirstName());
    assertEquals(user.getLastName(), userProfileRequest.getLastName());
  }

  @Test
  public void testMapEntityModelToResponseModel() {

    User user = new User("Username", "Password", "Email", "First Name", "Last Name");

    UserProfileResponse userProfileResponse = userProfileMapper.mapEntityModelToResponseModel(user);

    assertEquals("", userProfileResponse.getPassword());
    assertEquals(user.getEmail(), userProfileResponse.getEmail());
    assertEquals(user.getFirstName(), userProfileResponse.getFirstName());
    assertEquals(user.getLastName(), userProfileResponse.getLastName());
  }

  @Test
  public void testMapRequestModelToEntityModel() {

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
  public void testMapRequestModelToResponseModel() {

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
