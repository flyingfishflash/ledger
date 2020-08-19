package net.flyingfishflash.ledger.users.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import net.flyingfishflash.ledger.users.data.User;
import net.flyingfishflash.ledger.users.data.dto.UserCreateRequest;
import net.flyingfishflash.ledger.users.data.dto.UserCreateResponse;
import net.flyingfishflash.ledger.users.data.dto.UserDeleteResponse;
import net.flyingfishflash.ledger.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/ledger/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @ResponseBody
  @ApiOperation(value = "Retrieve all users")
  public Collection<User> findAll() {

    return userService.findAllUsers();
  }

  @GetMapping("{id}/profile")
  @ResponseBody
  @ApiOperation(value = "Retrieve profile of a single user by id")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
  public UserProfileResponse profileById(@PathVariable("id") Long id) {

    return userService.profileById(id);
  }

  @GetMapping("/profile")
  @ResponseBody
  @ApiOperation(value = "Retrieve profile of a single user by authenticated user name")
  public UserProfileResponse profileByUsername(Principal principal) {

    return userService.profileByUsername(principal.getName());
  }

  @PatchMapping("{id}")
  @ResponseBody
  @ApiOperation(value = "Update the profile information of a single user")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
  public UserProfileResponse profilePatch(
      @PathVariable("id") Long id, @RequestBody Map<String, Object> patchRequest) {

    // NOTE: This does not refresh the user details association with the principal
    // in the SecurityContext. The user should probably be asked to re-auth...
    // https://stackoverflow.com/questions/9910252/how-to-reload-authorities-on-user-update-with-spring-security

    return userService.profilePatch(id, patchRequest);
  }

  @PostMapping
  @ApiOperation(value = "Create a new user.")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
  public ResponseEntity<UserCreateResponse> signUp(
      @Valid @RequestBody UserCreateRequest userSignUpRequest) {

    userService.createUser(userSignUpRequest);

    return new ResponseEntity<>(
        new UserCreateResponse("Created user: " + userSignUpRequest.getUsername()),
        HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/delete")
  @ApiOperation(value = "Delete a user")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
  public ResponseEntity<UserDeleteResponse> deleteById(@RequestParam Long id) {

    userService.deleteById(id);

    return ResponseEntity.ok(new UserDeleteResponse("Deleted user: " + id));
  }
}
