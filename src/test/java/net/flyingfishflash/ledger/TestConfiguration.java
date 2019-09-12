package net.flyingfishflash.ledger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@Configuration
//@TestPropertySource(locations = "classpath:application.yml1")
//@DataJpaTest
//@EnableConfigurationProperties
//@TestPropertySource(locations = "classpath:application.yml1")
public class TestConfiguration {

  @PersistenceContext EntityManager entityManager;

/*  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder =
        new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName(UUID.randomUUID().toString());
    return builder.build();
  }

  @Bean
  public Properties additionalProperties() {
    Properties properties = new Properties();

    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.transaction.flush_before_completion", "false");
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    properties.setProperty("hibernate.generate_statistics", "false");
    properties.setProperty("hibernate.cache.use_second_level_cache", "false");
    //properties.setProperty("hibernate.hbm2ddl.import_files", "/fixtures/test-import.sql");
    return properties;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource, Properties additionalProperties) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("net.flyingfishflash.ledger");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setJpaProperties(additionalProperties);
    return em;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }*/

/*  @Bean
  // Why do I have to cast this DelegatingNestedNodeRepository?
  public DelegatingNestedNodeRepository<Long, Account> repository() {
    return (DelegatingNestedNodeRepository<Long, Account>)
        NestedNodeRepository.createDiscriminated(
            Long.class, Account.class, entityManager, new AccountTreeDiscriminator());
  }*/

/*  @Bean
  public AccountRepository accountRepository() {
    return new AccountRepository();
  }*/
}
