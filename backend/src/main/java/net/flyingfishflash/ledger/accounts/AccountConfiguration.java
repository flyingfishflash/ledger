package net.flyingfishflash.ledger.accounts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.nestedset.NestedNodeRepository;
import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.factory.JpaNestedNodeRepositoryFactory;
import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.commodities.data.Commodity;

@Configuration
@ComponentScan(
    basePackages = {
      "net.flyingfishflash.ledger.foundation",
      "net.flyingfishflash.ledger.accounts",
      "net.flyingfishflash.ledger.books"
    })
// JPA entity scan specification necessary for testing since commodity class is outside this package
@EntityScan(basePackageClasses = {Account.class, Book.class, Commodity.class})
public class AccountConfiguration {

  @PersistenceContext private EntityManager entityManager;

  @Bean
  public NestedNodeRepository<Long, Account> jpaRepository() {

    JpaNestedNodeRepositoryConfiguration<Long, Account> configuration =
        new JpaNestedNodeRepositoryConfiguration<>(entityManager, Account.class, Long.class);

    return JpaNestedNodeRepositoryFactory.create(configuration);
  }
}
