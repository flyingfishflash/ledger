package net.flyingfishflash.ledger.foundation.users.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.flyingfishflash.ledger.foundation.users.data.dto.UserProfileResponse;
import net.flyingfishflash.ledger.foundation.users.service.UserService;

@SpringBootTest(
    classes = UserTestConfiguration.class,
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=true",
    })
class UserControllerTests {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired UserService userService;

  @Test
  void testProfileByUsername() {

    ResponseEntity<UserProfileResponse> userProfileResponse =
        restTemplate
            .withBasicAuth("testuser", "TestUser1@")
            .getForEntity("/api/v1/ledger/users/profile", UserProfileResponse.class);

    assertEquals(HttpStatus.OK, userProfileResponse.getStatusCode());
  }
}
