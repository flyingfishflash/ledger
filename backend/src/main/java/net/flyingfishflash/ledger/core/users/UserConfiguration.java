package net.flyingfishflash.ledger.core.users;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.core.users.data.dto.UserProfileMapper;

@Configuration
public class UserConfiguration {

  @Bean
  public UserProfileMapper userProfileMapper() {
    return new UserProfileMapper();
  }
}
