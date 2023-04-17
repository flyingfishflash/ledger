package net.flyingfishflash.ledger.core;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import net.flyingfishflash.ledger.ApplicationConfiguration;
import net.flyingfishflash.ledger.core.users.data.UserRepository;

@Component
@Order(1)
@PropertySource("classpath:application.yaml")
public class CustomCommandLineRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(CustomCommandLineRunner.class);

  private final UserRepository userRepository;
  private final DataSource dataSource;

  public CustomCommandLineRunner(UserRepository userRepository, DataSource dataSource) {
    this.userRepository = userRepository;
    this.dataSource = dataSource;
  }

  @Override
  public void run(String... args) {
    logger.info(
        "{} Spring Boot version: {}",
        ApplicationConfiguration.LOGGER_PREFIX,
        SpringBootVersion.getVersion());
    migrateUserSchemas();
  }

  private void migrateUserSchemas() {
    logger.info(
        "{} Migrating database schemas for each user...", ApplicationConfiguration.LOGGER_PREFIX);
    userRepository
        .findAll()
        .forEach(
            user -> {
              String tenant = user.getUsername();
              var flyway =
                  Flyway.configure()
                      .locations("db/migration/users")
                      .dataSource(dataSource)
                      .schemas(tenant)
                      .load();
              flyway.migrate();
            });
  }
}
