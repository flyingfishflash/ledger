package net.flyingfishflash.ledger.integration.domain.accounts.nestedset;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import jakarta.persistence.EntityManager;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.NestedNodeRepository;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.factory.JpaNestedNodeRepositoryFactory;
import net.flyingfishflash.ledger.integration.domain.accounts.nestedset.config.jpa.discriminator.TestJpaTreeDiscriminator;
import net.flyingfishflash.ledger.integration.domain.accounts.nestedset.model.TestNode;

// @SpringBootConfiguration()
// @EnableAutoConfiguration
// @Import({TestJpaTreeDiscriminator.class, TestNode.class})
public class NestedSetTestConfiguration {

  // @Bean
  public Properties additionalProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    properties.setProperty("hibernate.show_sql", "false");
    properties.setProperty("hibernate.transaction.flush_before_completion", "false");
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    properties.setProperty("hibernate.generate_statistics", "false");
    properties.setProperty("hibernate.cache.use_second_level_cache", "false");
    properties.setProperty(
        "hibernate.hbm2ddl.import_files", "/db/migration/nestedset/nestedset-test.sql");
    return properties;
  }

  // @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource, Properties additionalProperties) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("net.flyingfishflash.ledger.nestedset");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setJpaProperties(additionalProperties);
    return em;
  }

  // potentially add a Flyway bean if enabled in context

  //  @Bean
  //  @Jpa
  public NestedNodeRepository<Long, TestNode> jpaRepository(EntityManager entityManager) {
    JpaNestedNodeRepositoryConfiguration<Long, TestNode> configuration =
        new JpaNestedNodeRepositoryConfiguration<>(
            entityManager, TestNode.class, Long.class, new TestJpaTreeDiscriminator());
    return JpaNestedNodeRepositoryFactory.create(configuration);
  }
}
