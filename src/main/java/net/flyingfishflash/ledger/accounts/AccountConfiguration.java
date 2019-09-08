package net.flyingfishflash.ledger.accounts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.exsio.nestedj.repository.DelegatingNestedNodeRepository;
import pl.exsio.nestedj.repository.NestedNodeRepository;

@Configuration
@ComponentScan
public class AccountConfiguration {

  @PersistenceContext EntityManager entityManager;

  @Bean
  // Why do I have to cast this DelegatingNestedNodeRepository?
  public DelegatingNestedNodeRepository<Long, Account> nestedNodeRepository() {
    return (DelegatingNestedNodeRepository<Long, Account>)
        NestedNodeRepository.createDiscriminated(
            Long.class, Account.class, entityManager, new AccountTreeDiscriminator());
  }
}
