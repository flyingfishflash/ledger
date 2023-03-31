package net.flyingfishflash.ledger.core.users.data.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateRequest {
  @NotBlank
  @Size(min = 6, max = 20)
  private String username;

  @NotBlank
  @Size(min = 8, max = 128)
  private String password;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  private Set<String> roles;

  public UserCreateRequest() {}

  public UserCreateRequest(
      String username,
      String password,
      String email,
      String firstName,
      String lastName,
      Set<String> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<String> getRoles() {
    return this.roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "UserCreateRequest{"
        + "username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", email='"
        + email
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", roles="
        + roles
        + '}';
  }
}
