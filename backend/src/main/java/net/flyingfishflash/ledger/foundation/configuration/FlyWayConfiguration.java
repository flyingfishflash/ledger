package net.flyingfishflash.ledger.foundation.configuration;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.foundation.multitenancy.TenantIdentifierResolver;

@Configuration
public class FlyWayConfiguration {

  @Bean
  public Flyway flyway(DataSource dataSource) {
    var flyway =
        Flyway.configure()
            .locations("db/migration/" + TenantIdentifierResolver.COMMON)
            .dataSource(dataSource)
            .schemas(TenantIdentifierResolver.COMMON)
            .load();
    flyway.migrate();
    return flyway;
  }
}
