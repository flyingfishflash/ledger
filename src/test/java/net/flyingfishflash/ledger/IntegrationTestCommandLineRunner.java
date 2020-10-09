package net.flyingfishflash.ledger;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class IntegrationTestCommandLineRunner implements CommandLineRunner {

  @Autowired DataSource dataSource;

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
