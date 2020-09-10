package net.flyingfishflash.ledger.foundation;

import javax.sql.DataSource;
import net.flyingfishflash.ledger.foundation.multitenancy.TenantIdentifierResolver;
import net.flyingfishflash.ledger.foundation.users.data.UserRepository;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CustomCommandLineRunner implements CommandLineRunner {

  private UserRepository userRepository;
  private DataSource dataSource;

  public CustomCommandLineRunner(UserRepository userRepository, DataSource dataSource) {
    this.userRepository = userRepository;
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) throws Exception {
    userRepository
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
  }
}
