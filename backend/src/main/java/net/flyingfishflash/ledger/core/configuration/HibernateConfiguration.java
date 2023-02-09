package net.flyingfishflash.ledger.core.configuration;

// import org.hibernate.MultiTenancyStrategy;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
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

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      @Autowired JpaProperties jpaProperties,
      MultiTenantConnectionProvider multiTenantConnectionProvider,
      CurrentTenantIdentifierResolver tenantIdentifierResolver) {

    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    // jpaPropertiesMap.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    jpaPropertiesMap.put(
        AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    jpaPropertiesMap.put(
        AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);

    var entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource);
    entityManagerFactoryBean.setPackagesToScan(Application.class.getPackage().getName());
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    entityManagerFactoryBean.setJpaPropertyMap(jpaPropertiesMap);
    return entityManagerFactoryBean;
  }
}
