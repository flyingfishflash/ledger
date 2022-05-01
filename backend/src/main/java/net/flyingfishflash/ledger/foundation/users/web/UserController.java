package net.flyingfishflash.ledger.foundation.users.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserCreateRequest;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserCreateResponse;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserDeleteResponse;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.foundation.users.service.UserService;

@Tag(name = "user controller")
@RestController
@Validated
@RequestMapping("/api/v1/ledger/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @ResponseBody
  @Operation(summary = "Retrieve all users")
  public Collection<User> findAll() {

    return userService.findAllUsers();
  }

  @GetMapping("{id}/profile")
  @ResponseBody
  @Operation(summary = "Retrieve profile of a single user by id")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public UserProfileResponse profileById(@PathVariable("id") Long id) {

    return userService.profileById(id);
  }

  @GetMapping("/profile")
  @ResponseBody
  @Operation(summary = "Retrieve profile of a single user by authenticated user name")
  public UserProfileResponse profileByUsername(Principal principal) {

    return userService.profileByUsername(principal.getName());
  }

  @PatchMapping("{id}")
  @ResponseBody
  @Operation(summary = "Update the profile information of a single user")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public UserProfileResponse profilePatch(
      @PathVariable("id") Long id, @RequestBody Map<String, Object> patchRequest) {

    // NOTE: This does not refresh the user details association with the principal
    // in the SecurityContext. The user should probably be asked to re-auth...
    // https://stackoverflow.com/questions/9910252/how-to-reload-authorities-on-user-update-with-spring-security

    return userService.profilePatch(id, patchRequest);
  }

  @PostMapping
  @Operation(summary = "Create a new user.")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public ResponseEntity<UserCreateResponse> signUp(
      @Valid @RequestBody UserCreateRequest userSignUpRequest) {

    userService.createUser(userSignUpRequest);

    return new ResponseEntity<>(
        new UserCreateResponse("Created user: " + userSignUpRequest.getUsername()),
        HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/delete")
  @Operation(summary = "Delete a user")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<UserDeleteResponse> deleteById(@RequestParam Long id) {

    userService.deleteById(id);

    return new ResponseEntity<>(
        new UserDeleteResponse("Deleted user id: " + id), HttpStatus.NO_CONTENT);
  }
}
