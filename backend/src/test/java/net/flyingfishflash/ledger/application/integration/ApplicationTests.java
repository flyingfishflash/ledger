package net.flyingfishflash.ledger.application.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Tag("Integration")
@ComponentScan(
    basePackages = {
      "net.flyingfishflash.ledger",
    })
@EnableJpaRepositories(basePackages = {"net.flyingfishflash.ledger"})
@SpringBootTest(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
// @SpringBootConfiguration
// @EnableAutoConfiguration
class ApplicationTests {

  @Test
  void applicationContextLoads(ApplicationContext applicationContext) {
    assertThat(applicationContext).isNotNull();
    System.out.println("\nApplication Integration Test\n");
  }
}
