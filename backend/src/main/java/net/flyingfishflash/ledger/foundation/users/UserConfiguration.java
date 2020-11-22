package net.flyingfishflash.ledger.foundation.users;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.foundation.users.data.Role;
import net.flyingfishflash.ledger.foundation.users.data.User;
import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileMapper;

@Configuration
@ComponentScan(basePackages = "net.flyingfishflash.ledger.foundation")
@EntityScan(
    basePackageClasses = {
      User.class,
      Role.class,
    })
public class UserConfiguration {

  @Bean
  public UserProfileMapper userProfileMapper() {
    return new UserProfileMapper();
  }
}
