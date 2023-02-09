package net.flyingfishflash.ledger.domain.prices.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Tag("Integration")
@SpringBootTest(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
    })
class PriceTestConfigurationTest {

  @BeforeEach
  void printApplicationContext() {}

  @Test
  void priceConfigurationLoads(ApplicationContext applicationContext) {
    assertThat(applicationContext).isNotNull();
    System.out.println("\nPrice Integration Test\n");
  }
}
