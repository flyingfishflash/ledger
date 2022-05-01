package net.flyingfishflash.ledger.prices.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Tag("Integration")
@SpringBootTest(
    classes = PriceTestConfiguration.class,
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
class PriceTestConfigurationTest {

  @BeforeEach
  void printApplicationContext() {
    /*
        Arrays.stream(applicationContext.getBeanDefinitionNames())
            .map(name -> applicationContext.getBean(name).getClass().getName())
            .sorted()
            .forEach(System.out::println);
    */
  }

  @Test
  void priceConfigurationLoads(ApplicationContext applicationContext) {
    assertThat(applicationContext).isNotNull();
    System.out.println("\nPrice Integration Test\n");
  }
}
