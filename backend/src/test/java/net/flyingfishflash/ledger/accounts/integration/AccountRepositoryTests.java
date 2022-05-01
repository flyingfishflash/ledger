package net.flyingfishflash.ledger.accounts.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import net.flyingfishflash.ledger.IntegrationTestCommandLineRunner;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.AccountType;

@Tag("Integration")
@SpringBootTest(
    classes = {AccountTestConfiguration.class, IntegrationTestCommandLineRunner.class},
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      // "spring.flyway.enabled=false",
      // "spring.jpa.hibernate.ddl-auto=create-drop",
    })
@WithMockUser(value = "testuser")
class AccountRepositoryTests {

  @Autowired AccountRepository accountRepository;

  @Test
  void findRoot() {
    Optional<Account> account = accountRepository.findRoot();
    assertThat(account).isPresent();
    assertThat(account.get())
        .hasFieldOrPropertyWithValue("parentId", null)
        .hasFieldOrPropertyWithValue("category", AccountCategory.ROOT)
        .hasFieldOrPropertyWithValue("type", AccountType.ROOT);
  }

  @Test
  void findById() {
    var id = 32L;
    Optional<Account> account = accountRepository.findById(id);
    assertThat(account).isPresent();
    assertThat(account.get()).hasFieldOrPropertyWithValue("id", id);
  }

  @Test
  void findByGuid() {
    var guid = "ea2c06e9dd20e09797b025d24deca332";
    Optional<Account> account = accountRepository.findByGuid(guid);
    assertThat(account).isPresent();
    assertThat(account.get()).hasFieldOrPropertyWithValue("guid", guid);
  }

  @Test
  void rootLevelNodeCount() {
    assertThat(accountRepository.rootLevelNodeCount()).isEqualTo(1L);
  }
}
