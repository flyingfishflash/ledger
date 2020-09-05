package net.flyingfishflash.ledger.accounts.integration;

import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.foundation.users.data.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(
    classes = AccountTestConfiguration.class,
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
public class AccountTestConfigurationTest {

  @Autowired private ApplicationContext applicationContext;

  @Autowired private AccountRepository accountRepository;

  @Autowired private UserRepository userRepository;

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
  void accountConfigurationLoads() {
    System.out.println("\nAccount Integration Test\n");
  }

  @WithMockUser(value = "testuser")
  @Test
  void accountConfigurationLoads2() {
    System.out.println("\nAccount Integration Test 2\n");
    accountRepository.findRoot();
  }
}
