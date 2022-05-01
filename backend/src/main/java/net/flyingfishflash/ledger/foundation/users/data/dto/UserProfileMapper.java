package net.flyingfishflash.ledger.foundation.users.data.dto;

import net.flyingfishflash.ledger.foundation.users.data.User;

public class UserProfileMapper {

  public UserProfileRequest mapEntityModelToRequestModel(User user) {
    var userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail(user.getEmail());
    userProfileRequest.setFirstName(user.getFirstName());
    userProfileRequest.setLastName(user.getLastName());
    userProfileRequest.setPassword(user.getPassword()); // encrypt me
    return userProfileRequest;
  }

  public UserProfileResponse mapEntityModelToResponseModel(User user) {
    return new UserProfileResponse(
        user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null);
  }

  public void mapRequestModelToEntityModel(UserProfileRequest userProfileRequest, User user) {
    user.setEmail(userProfileRequest.getEmail());
    user.setFirstName(userProfileRequest.getFirstName());
    user.setLastName(userProfileRequest.getLastName());
    user.setPassword(userProfileRequest.getPassword());
    // return user;
  }

  public UserProfileResponse mapRequestModelToResponseModel(UserProfileRequest userProfileRequest) {
    return new UserProfileResponse(
        null,
        userProfileRequest.getEmail(),
        userProfileRequest.getFirstName(),
        userProfileRequest.getLastName(),
        null);
  }
}
