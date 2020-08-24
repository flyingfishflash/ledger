/*
package net.flyingfishflash.ledger.foundation.users.integration;

import net.flyingfishflash.ledger.foundation.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    classes = UserTestConfiguration.class,
    properties = {
      // "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=true",
    })
public class UserTestConfigurationTest {

  @Autowired UserService userService;

  */
/*
    @Autowired ApplicationContext applicationContext;

    @BeforeEach
    void printApplicationContext() {
      Arrays.stream(applicationContext.getBeanDefinitionNames())
          .map(name -> applicationContext.getBean(name).getClass().getName())
          .sorted()
          .forEach(System.out::println);
    }
  *//*


  @Test
  void userConfigurationLoads() {
    System.out.println("\nUser Integration Test\n");
    System.out.println(userService.findById(1L));
  }
}
*/
