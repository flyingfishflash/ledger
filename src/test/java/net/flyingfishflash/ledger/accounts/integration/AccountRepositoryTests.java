package net.flyingfishflash.ledger.accounts.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import net.flyingfishflash.ledger.IntegrationTestCommandLineRunner;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(
    classes = {AccountTestConfiguration.class, IntegrationTestCommandLineRunner.class},
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
@WithMockUser(value = "testuser")
public class AccountRepositoryTests {

  @Autowired AccountRepository accountRepository;

  @Test
  void testFindRoot() {
    Optional<Account> account = accountRepository.findRoot();
    assertTrue(account.isPresent());
    assertNull(account.get().getParentId());
    assertEquals(AccountCategory.Root, account.get().getCategory());
    assertEquals(AccountType.Root, account.get().getType());
  }

  @Test
  void testFindById() {
    Long id = 32L;
    Optional<Account> account = accountRepository.findById(id);
    assertTrue(account.isPresent());
    assertEquals(id, account.get().getId());
  }

  @Test
  void testFindByGuid() {
    String guid = "ea2c06e9dd20e09797b025d24deca332";
    Optional<Account> account = accountRepository.findByGuid(guid);
    assertTrue(account.isPresent());
    assertEquals(guid, account.get().getGuid());
  }

  @Test
  void testRootLevelNodeCount() {
    assertEquals(1L, accountRepository.rootLevelNodeCount());
  }
}
