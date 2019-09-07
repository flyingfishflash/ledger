package net.flyingfishflash.ledger.accounts;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.flyingfishflash.ledger.Application;
import net.flyingfishflash.ledger.accounts.Account;
import net.flyingfishflash.ledger.accounts.AccountRepository;
import net.flyingfishflash.ledger.accounts.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.exsio.nestedj.repository.DelegatingNestedNodeRepository;

/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
*/

@ExtendWith({SpringExtension.class/*, MockitoExtension.class*/})
//@ContextConfiguration(classes = {TestConfiguration.class})
//@EnableConfigurationProperties
//@SpringBootTest(classes = AccountService.class)
//@ActiveProfiles("test")
//@ContextConfiguration(classes = {Application.class})
@SpringBootTest(classes = {Application.class})
//@DataJpaTest
// @WebMvcTest(value = AccountController.class)
public class AccountServiceTests {

  @Autowired protected DelegatingNestedNodeRepository<Long, Account> nodeRepository;

  @Autowired
  AccountRepository accountRepository;

  @PersistenceContext protected EntityManager em;

  @Autowired
  AccountService accountService = new AccountService(accountRepository);

  @Test
  public void testAccountService1() {

    Optional<Account> z =  accountRepository.findOneById(1L);
    System.out.println(z.toString());
    //Account t = accountService.findById(2L);


  }

/*  @Test
  public void contextLoads() {

    // assertThat()
  }*/
}
// }
