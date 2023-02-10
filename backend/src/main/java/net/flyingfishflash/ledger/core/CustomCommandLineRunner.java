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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.flyingfishflash.ledger.core.multitenancy.TenantIdentifierResolver;
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
  public void run(String... args) throws Exception {
    userRepository
        .findAll()
        .forEach(
            user -> {
              String tenant = user.getUsername();
              var flyway =
                  Flyway.configure()
                      .locations("db/migration/" + TenantIdentifierResolver.USERS)
                      .dataSource(dataSource)
                      .schemas(tenant)
                      .load();
              flyway.migrate();
            });

    setApiUrl();
  }

  private void setApiUrl() throws IOException {
    var envApiServer = "API_SERVER";
    var apiUrlPath = env.getProperty("config.server.api.url-path");
    String apiUrl;
    String apiServer;
    String apiServerDefault = "http://localhost:" + env.getProperty("server.port");
    String apiUrlDefault = apiServerDefault + apiUrlPath;
    String apiServerOriginal;

    apiServerOriginal = System.getenv(envApiServer);

    if (System.getenv(envApiServer) == null
        || System.getenv(envApiServer).isEmpty()
        || System.getenv(envApiServer).isBlank()) {
      logger.info(
          "System environment variable {} not set. Using default value: {}",
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
            "API URL is invalid. (API_SERVER}: {}, Derived Url: {}", apiServerOriginal, apiUrl);
        logger.error("API URL: using default value");
        apiUrl = apiUrlDefault;
      }
    }

    logger.info("API URL: {}", apiUrl);

    String path = Objects.requireNonNull(getClass().getResource("/static/assets")).getFile();
    var file = new File(path + "/config.json");

    logger.info("{}", file);

    try (var bufferedWriter =
        Files.newBufferedWriter(Paths.get(String.valueOf(file.getAbsoluteFile())))) {
      bufferedWriter.write("{\n\t\"server\": {\n\t\t\"url\": \"" + apiUrl + "\"\n\t}\n}");
    } catch (IOException e) {
      logger.error("failed to write config.json");
    }
  }
}
