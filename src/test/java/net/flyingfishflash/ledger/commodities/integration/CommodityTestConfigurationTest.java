package net.flyingfishflash.ledger.commodities.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(
    classes = CommodityTestConfiguration.class,
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
public class CommodityTestConfigurationTest {

  @Autowired private ApplicationContext applicationContext;

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
  void commodityConfigurationLoads() {
    System.out.println("\nCommodity Integration Test\n");
  }
}
