package net.flyingfishflash.ledger.users;

import net.flyingfishflash.ledger.users.data.Role;
import net.flyingfishflash.ledger.users.data.User;
import net.flyingfishflash.ledger.users.data.dto.UserProfileMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EntityScan(basePackageClasses = {User.class, Role.class})
public class UserConfiguration {

  @Bean
  public UserProfileMapper userProfileMapper() {
    return new UserProfileMapper();
  }
}
