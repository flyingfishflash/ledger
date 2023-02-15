package net.flyingfishflash.ledger.integration.core.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.flyingfishflash.ledger.core.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.core.users.service.UserService;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {"spring.main.allow-bean-definition-overriding=true"})
class UserControllerTests {

  @Value("${config.application.api-v1-url-path}")
  private String apiPrefix;

  @Autowired private TestRestTemplate restTemplate;
  @Autowired UserService userService;

  @Test
  void profileByUsername() {
    ResponseEntity<UserProfileResponse> userProfileResponse =
        restTemplate
            .withBasicAuth("testuser", "TestUser1@")
            .getForEntity(String.format("%s/users/profile", apiPrefix), UserProfileResponse.class);
    assertThat(userProfileResponse).hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK);
  }
}
