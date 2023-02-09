package net.flyingfishflash.ledger.application.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Tag("Integration")
@EnableJpaRepositories(basePackages = {"net.flyingfishflash.ledger"})
@SpringBootTest(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class ApplicationTests {

  @Test
  void applicationContextLoads(ApplicationContext applicationContext) {
    assertThat(applicationContext).isNotNull();
    System.out.println("\nApplication Integration Test\n");
  }
}
