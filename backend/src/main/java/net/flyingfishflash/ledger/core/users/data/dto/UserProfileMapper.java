package net.flyingfishflash.ledger.core.users.data.dto;

import net.flyingfishflash.ledger.core.users.data.User;

public class UserProfileMapper {

  public UserProfileRequest mapEntityModelToRequestModel(User user) {

    return new UserProfileRequest(
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getPassword() /* TODO: encrypt this password field */);
  }

  public UserProfileResponse mapEntityModelToResponseModel(User user) {
    return new UserProfileResponse(
        user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null);
  }

  public void mapRequestModelToEntityModel(UserProfileRequest userProfileRequest, User user) {
    user.setEmail(userProfileRequest.email());
    user.setFirstName(userProfileRequest.firstName());
    user.setLastName(userProfileRequest.lastName());
    user.setPassword(userProfileRequest.password());
    // return user;
  }

  public UserProfileResponse mapRequestModelToResponseModel(UserProfileRequest userProfileRequest) {
    return new UserProfileResponse(
        null,
        userProfileRequest.email(),
        userProfileRequest.firstName(),
        userProfileRequest.lastName(),
        null);
  }
}
