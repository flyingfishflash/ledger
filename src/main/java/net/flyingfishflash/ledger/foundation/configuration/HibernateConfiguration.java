package net.flyingfishflash.ledger.foundation.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import net.flyingfishflash.ledger.Application;

@Configuration
public class HibernateConfiguration {

  @Autowired private JpaProperties jpaProperties;

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      // JpaProperties jpaProperties,
      MultiTenantConnectionProvider multiTenantConnectionProvider,
      CurrentTenantIdentifierResolver tenantIdentifierResolver) {

    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    jpaPropertiesMap.put(
        Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan(Application.class.getPackage().getName());
    em.setJpaVendorAdapter(jpaVendorAdapter());
    em.setJpaPropertyMap(jpaPropertiesMap);
    return em;
  }
}
