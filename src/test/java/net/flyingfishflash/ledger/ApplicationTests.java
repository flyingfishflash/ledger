package net.flyingfishflash.ledger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {Application.class})
@SpringBootTest(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      /*"spring.jpa.hibernate.ddl-auto=create-drop",
          "spring.liquibase.enabled=false",
          "spring.flyway.enabled=false"
      */ })
class ApplicationTests {

  @Test
  void applicationContextLoads() {}
}
