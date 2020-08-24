package net.flyingfishflash.ledger.accounts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountTreeDiscriminator;
import net.flyingfishflash.ledger.commodities.data.Commodity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.exsio.nestedj.NestedNodeRepository;
import pl.exsio.nestedj.config.jpa.JpaNestedNodeRepositoryConfiguration;
import pl.exsio.nestedj.config.jpa.factory.JpaNestedNodeRepositoryFactory;

@Configuration
@ComponentScan(
    basePackages = {
        "net.flyingfishflash.ledger.foundation",
        "net.flyingfishflash.ledger.accounts",
    })
// JPA entity scan specification necessary for testing since commodity class is outside this package
@EntityScan(basePackageClasses = {Account.class, Commodity.class})
public class AccountConfiguration {

  @PersistenceContext private EntityManager entityManager;

  @Bean
  public NestedNodeRepository<Long, Account> jpaRepository() {

    JpaNestedNodeRepositoryConfiguration<Long, Account> configuration =
        new JpaNestedNodeRepositoryConfiguration<>(
            entityManager, Account.class, Long.class, new AccountTreeDiscriminator());

    return JpaNestedNodeRepositoryFactory.create(configuration);
  }
}
