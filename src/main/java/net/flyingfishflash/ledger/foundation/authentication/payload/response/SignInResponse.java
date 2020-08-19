package net.flyingfishflash.ledger.foundation.authentication.payload.response;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public class SignInResponse {
  private Long id;
  private String username;
  private List<String> roles;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return this.roles;
  }

  public void setRoles(List<GrantedAuthority> authorities) {
    List<String> newAuthorities = new ArrayList<>();
    authorities.forEach(
        role -> newAuthorities.add((authorities.toString().replace("[", "").replace("]", ""))));
    this.roles = newAuthorities;
  }

  @Override
  public String toString() {
    return "SignInResponse{"
        + "id="
        + id
        + ", username='"
        + username
        + '\''
        + ", roles="
        + roles
        + '}';
  }
}
