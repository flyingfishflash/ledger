package net.flyingfishflash.ledger.core;

import static org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.validator.routines.UrlValidator;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.flyingfishflash.ledger.ApplicationConfiguration;
import net.flyingfishflash.ledger.core.users.data.UserRepository;

@Component
@Order(1)
@PropertySource("classpath:application.yaml")
public class CustomCommandLineRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(CustomCommandLineRunner.class);

  private final UserRepository userRepository;
  private final DataSource dataSource;
  private final Environment env;

  public CustomCommandLineRunner(
      UserRepository userRepository, DataSource dataSource, Environment env) {
    this.userRepository = userRepository;
    this.dataSource = dataSource;
    this.env = env;
  }

  @Override
  public void run(String... args) {
    logger.info(
        "{} Spring Boot version: {}",
        ApplicationConfiguration.LOGGER_PREFIX,
        SpringBootVersion.getVersion());
    migrateUserSchemas();
    setApiUrl();
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

  private void setApiUrl() {
    var envApiServer = "API_SERVER";
    var apiUrlPath = env.getProperty("config.application.api-v1-url-path");
    var apiServerDefault = String.format("http://localhost:%s", env.getProperty("server.port"));
    var apiUrlDefault = apiServerDefault + apiUrlPath;
    var apiServerOriginal = System.getenv(envApiServer);
    String apiUrl;
    String apiServer;

    if (System.getenv(envApiServer) == null
        || System.getenv(envApiServer).isEmpty()
        || System.getenv(envApiServer).isBlank()) {
      logger.info(
          "{} System environment variable {} not set. Using default value: {}",
          ApplicationConfiguration.LOGGER_PREFIX,
          envApiServer,
          apiUrlDefault);
      apiUrl = apiUrlDefault;
    } else {

      apiServer = System.getenv(envApiServer);

      if (apiServer.endsWith("/")) {
        apiServer = apiServer.substring(0, apiServer.length() - 1);
      }

      if (StringUtils.countOccurrencesOf(apiServer, "/") != 2) {
        logger.error("{} from environment is invalid. ({})", envApiServer, apiServerOriginal);
        logger.error("{}: using default value", envApiServer);
        apiServer = apiServerDefault;
      }

      apiUrl = apiServer + apiUrlPath;

      var schemes = new String[] {"http", "https"};
      var urlValidator = new UrlValidator(schemes, ALLOW_LOCAL_URLS);

      if (!urlValidator.isValid(apiUrl)) {
        logger.error(
            "{} API URL is invalid. (API_SERVER}: {}, Derived Url: {}",
            ApplicationConfiguration.LOGGER_PREFIX,
            apiServerOriginal,
            apiUrl);
        logger.error("{} URL: using default value", ApplicationConfiguration.LOGGER_PREFIX);
        apiUrl = apiUrlDefault;
      }
    }

    logger.info("{} API URL: {}", ApplicationConfiguration.LOGGER_PREFIX, apiUrl);

    String path = Objects.requireNonNull(getClass().getResource("/static/assets")).getFile();
    var file = new File(path + "/config.json");

    logger.info("{} {}", ApplicationConfiguration.LOGGER_PREFIX, file);

    try (var bufferedWriter =
        Files.newBufferedWriter(Paths.get(String.valueOf(file.getAbsoluteFile())))) {
      bufferedWriter.write("{\n\t\"server\": {\n\t\t\"url\": \"" + apiUrl + "\"\n\t}\n}");
    } catch (IOException e) {
      logger.error("failed to write config.json");
    }
  }
}
