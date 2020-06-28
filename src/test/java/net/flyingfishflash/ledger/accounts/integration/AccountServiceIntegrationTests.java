package net.flyingfishflash.ledger.accounts.integration;

import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountServiceIntegrationTests {

  // @Mock
  // private AccountRepository accountRepository;

  @Autowired private AccountService accountService;
  @Autowired private AccountRepository accountRepository;

  /*
    Disable this integratino test stub since seeding of the database via src/main/java/resources/data.sql is not guaranteed

    @Test
    public void test() {
      System.out.println(accountRepository.findById(1L));
      System.out.println(accountService.findById(1L));
    }
  */
}
