package net.flyingfishflash.ledger.accounts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountTreeDiscriminator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.exsio.nestedj.NestedNodeRepository;
import pl.exsio.nestedj.config.jpa.JpaNestedNodeRepositoryConfiguration;
import pl.exsio.nestedj.jpa.repository.factory.JpaNestedNodeRepositoryFactory;

@Configuration
@ComponentScan
public class AccountConfiguration {

  @PersistenceContext EntityManager entityManager;

  @Bean
  public NestedNodeRepository<Long, Account> jpaRepository() {

    JpaNestedNodeRepositoryConfiguration<Long, Account> configuration =
        new JpaNestedNodeRepositoryConfiguration<>(
            entityManager, Account.class, Long.class, new AccountTreeDiscriminator());

    return JpaNestedNodeRepositoryFactory.create(configuration);
  }
}
