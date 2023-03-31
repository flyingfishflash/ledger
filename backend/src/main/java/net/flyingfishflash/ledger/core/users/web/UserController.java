package net.flyingfishflash.ledger.core.users.web;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.users.data.User;
import net.flyingfishflash.ledger.core.users.data.dto.*;
import net.flyingfishflash.ledger.core.users.service.UserService;

@Tag(name = "user controller")
@RestController
@Validated
@RequestMapping("/users")
@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @ResponseBody
  @Operation(
      summary = "Retrieve all users",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
      })
  public Response<Collection<User>> findAll(HttpServletRequest request) {

    return new Response<>(
        userService.findAllUsers(),
        "Retrieve all users",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }

  @GetMapping("{id}/profile")
  @ResponseBody
  @Operation(
      summary = "Retrieve profile of a single user by id",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
      })
  public Response<UserProfileResponse> profileById(
      @PathVariable("id") Long id, HttpServletRequest request) {

    return new Response<>(
        userService.profileById(id),
        "Profile of a single user by id",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }

  @GetMapping("/profile")
  @ResponseBody
  @Operation(
      summary = "Retrieve profile of a single user by authenticated user name",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
      })
  public Response<UserProfileResponse> profileByUsername(
      Principal principal, HttpServletRequest request) {

    return new Response<>(
        userService.profileByUsername(principal.getName()),
        "Retrieve profile of a single user by authenticated user name",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }

  @PatchMapping("{id}")
  @ResponseBody
  @Operation(
      summary = "Update the profile information of a single user",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
      })
  public Response<UserProfileResponse> profilePatch(
      @PathVariable("id") Long id,
      @RequestBody Map<String, Object> patchRequest,
      HttpServletRequest request) {

    // NOTE: This does not refresh the user details association with the principal
    // in the SecurityContext. The user should probably be asked to re-auth...
    // https://stackoverflow.com/questions/9910252/how-to-reload-authorities-on-user-update-with-spring-security

    return new Response<>(
        userService.profilePatch(id, patchRequest),
        "Update the profile information of a single user",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }

  @PostMapping
  @Operation(
      summary = "Create a new user.",
      responses = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true)))
      })
  public ResponseEntity<Response<String>> signUp(
      @Valid @RequestBody UserCreateRequest userSignUpRequest, HttpServletRequest request) {

    userService.createUser(userSignUpRequest);

    return new ResponseEntity<>(
        new Response<>(
            "Created user: " + userSignUpRequest.getUsername(),
            "MESSAGE FIX ME",
            request.getMethod(),
            URI.create(request.getRequestURI())),
        HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/delete")
  @Operation(
      summary = "Delete a user",
      responses = {
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
      })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Response<String>> deleteById(
      @RequestParam Long id, HttpServletRequest request) {

    userService.deleteById(id);

    return new ResponseEntity<>(
        new Response<>(
            String.format("Deleted user id: %s", id),
            "MESSAGE FIX ME",
            request.getMethod(),
            URI.create(request.getRequestURI())),
        HttpStatus.NO_CONTENT);
  }
}
