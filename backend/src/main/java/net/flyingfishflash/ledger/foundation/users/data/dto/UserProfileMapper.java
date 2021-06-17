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
    var userProfileResponse = new UserProfileResponse();
    userProfileResponse.setId(user.getId());
    userProfileResponse.setEmail(user.getEmail());
    userProfileResponse.setFirstName(user.getFirstName());
    userProfileResponse.setLastName(user.getLastName());
    userProfileResponse.setPassword("");
    return userProfileResponse;
  }

  public void mapRequestModelToEntityModel(UserProfileRequest userProfileRequest, User user) {
    user.setEmail(userProfileRequest.getEmail());
    user.setFirstName(userProfileRequest.getFirstName());
    user.setLastName(userProfileRequest.getLastName());
    user.setPassword(userProfileRequest.getPassword());
    // return user;
  }

  public UserProfileResponse mapRequestModelToResponseModel(UserProfileRequest userProfileRequest) {
    var userProfileResponse = new UserProfileResponse();
    userProfileResponse.setEmail(userProfileRequest.getEmail());
    userProfileResponse.setFirstName(userProfileRequest.getFirstName());
    userProfileResponse.setLastName(userProfileRequest.getLastName());
    return userProfileResponse;
  }
}
