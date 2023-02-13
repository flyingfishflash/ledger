package net.flyingfishflash.ledger;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.CommandLineRunner;

@Tag("Integration")
// @Component
public class IntegrationTestCommandLineRunner implements CommandLineRunner {

  private final DataSource dataSource;

  public IntegrationTestCommandLineRunner(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) throws Exception {
    Flyway flyway =
        Flyway.configure()
            .locations("db/migration/users_testuser")
            .dataSource(dataSource)
            .schemas("testuser")
            .load();
    flyway.migrate();
  }
}
