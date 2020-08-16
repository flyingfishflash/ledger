package net.flyingfishflash.ledger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {Application.class})
@SpringBootTest(
    properties = {"spring.main.allow-bean-definition-overriding=true", "spring.flyway.enabled=false"
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class ApplicationTests {

  @Test
  void applicationContextLoads() {}
}
