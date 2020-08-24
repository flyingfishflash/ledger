package net.flyingfishflash.ledger.foundation.configuration;

import javax.sql.DataSource;
import net.flyingfishflash.ledger.foundation.multitenancy.TenantIdentifierResolver;
import net.flyingfishflash.ledger.foundation.users.data.UserRepository;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
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

  @Bean
  CommandLineRunner commandLineRunner(UserRepository repository, DataSource dataSource) {
    return args -> {
      repository
          .findAll()
          .forEach(
              user -> {
                String tenant = user.getUsername();
                Flyway flyway =
                    Flyway.configure()
                        .locations("db/migration/" + TenantIdentifierResolver.USERS)
                        .dataSource(dataSource)
                        .schemas(tenant)
                        .load();
                flyway.migrate();
              });
    };
  }
}
