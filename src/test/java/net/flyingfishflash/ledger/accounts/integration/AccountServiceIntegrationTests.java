package net.flyingfishflash.ledger.accounts.integration;

import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountServiceIntegrationTests {


  //@Mock
  //private AccountRepository accountRepository;
  //private AccountRepository accountRepository;

@Autowired
private AccountRepository accountRepository;

@Autowired
AccountService accountService;

  @Test
  public void test() {
    System.out.println(accountRepository.findOneById(1L));
    System.out.println(accountService.findById(1L));
    //System.out.println(accountService.findById(7L));
  }
}
