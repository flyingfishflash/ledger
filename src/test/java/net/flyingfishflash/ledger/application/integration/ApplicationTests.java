/*
package net.flyingfishflash.ledger.application.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.test.context.support.WithMockUser;

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
*/
/*
@SpringBootConfiguration
@EnableAutoConfiguration
*//*

class ApplicationTests {

  @WithMockUser(value = "admin")
  @Test
  void applicationContextLoads() {
    System.out.println("\nApplication Integration Test\n");
  }
}
*/
