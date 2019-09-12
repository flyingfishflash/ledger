package net.flyingfishflash.ledger.accounts;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = AccountTestConfiguration.class)
class AccountTestConfigurationTest {

  @Autowired
  private ApplicationContext applicationContext;


  @BeforeEach
  void printApplicationContext() {
    Arrays.stream(applicationContext.getBeanDefinitionNames())
        .map(name -> applicationContext.getBean(name).getClass().getName())
        .sorted()
        .forEach(System.out::println);
  }
  @Test
  void accountConfigurationLoads() {
  }

}