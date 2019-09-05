package net.flyingfishflash.ledger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.flyingfishflash.ledger.accounts.Account;
import net.flyingfishflash.ledger.accounts.AccountTreeDiscriminator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.exsio.nestedj.repository.DelegatingNestedNodeRepository;
import pl.exsio.nestedj.repository.NestedNodeRepository;

@Configuration
public class LedgerConfiguration {

  @PersistenceContext EntityManager em;

  @Bean
  // Why do I have to cast this DelegatingNestedNodeRepository?
  public DelegatingNestedNodeRepository<Long, Account> repository() {
    return (DelegatingNestedNodeRepository<Long, Account>)
        NestedNodeRepository.createDiscriminated(
            Long.class, Account.class, em, new AccountTreeDiscriminator());
  }
}
