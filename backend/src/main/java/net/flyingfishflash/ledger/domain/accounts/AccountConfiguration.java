package net.flyingfishflash.ledger.domain.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import net.flyingfishflash.ledger.domain.accounts.data.Account;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.NestedNodeRepository;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.factory.JpaNestedNodeRepositoryFactory;

@Configuration
public class AccountConfiguration {

  @PersistenceContext private EntityManager entityManager;

  @Bean
  public NestedNodeRepository<Long, Account> jpaRepository() {

    JpaNestedNodeRepositoryConfiguration<Long, Account> configuration =
        new JpaNestedNodeRepositoryConfiguration<>(entityManager, Account.class, Long.class);

    return JpaNestedNodeRepositoryFactory.create(configuration);
  }
}
