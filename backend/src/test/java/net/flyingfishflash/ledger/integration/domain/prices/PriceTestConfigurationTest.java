package net.flyingfishflash.ledger.integration.domain.prices;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class PriceTestConfigurationTest {

  @BeforeEach
  void printApplicationContext() {}

  @Test
  void priceConfigurationLoads(ApplicationContext applicationContext) {
    assertThat(applicationContext).isNotNull();
    System.out.println("\nPrice Integration Test\n");
  }
}
