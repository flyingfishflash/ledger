package net.flyingfishflash.ledger.foundation.configuration;

import javax.sql.DataSource;
import net.flyingfishflash.ledger.foundation.multitenancy.TenantIdentifierResolver;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlyWayConfiguration {

  @Bean
  public Flyway flyway(DataSource dataSource) {
    Flyway flyway =
        Flyway.configure()
            .locations("db/migration/" + TenantIdentifierResolver.COMMON)
            .dataSource(dataSource)
            .schemas(TenantIdentifierResolver.COMMON)
            .load();
    flyway.migrate();
    return flyway;
  }
}
