package net.flyingfishflash.ledger.domain.commodities.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Tag("Integration")
@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class CommodityTestConfigurationTest {

  @SuppressWarnings("unused")
  @Autowired
  private ApplicationContext applicationContext;

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
  void commodityConfigurationLoads(ApplicationContext applicationContext) {
    assertThat(applicationContext).isNotNull();
    System.out.println("\nCommodity Integration Test\n");
  }
}
