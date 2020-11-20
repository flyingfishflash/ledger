package net.flyingfishflash.ledger.foundation;

import static org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

import net.flyingfishflash.ledger.foundation.multitenancy.TenantIdentifierResolver;
import net.flyingfishflash.ledger.foundation.users.data.UserRepository;

@Component
@Order(1)
@PropertySource("classpath:application.properties")
public class CustomCommandLineRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(CustomCommandLineRunner.class);

  private UserRepository userRepository;
  private DataSource dataSource;
  private Environment env;

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
              Flyway flyway =
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
    String envApiServer = "API_SERVER";
    String apiUrl;
    String apiUrlPath = "/api/v1/ledger";
    String apiServer;
    String apiServerDefault = "http://localhost:" + env.getProperty("server.port");
    String apiUrlDefault = apiServerDefault + apiUrlPath;
    String apiServerOriginal;

    apiServerOriginal = System.getenv(envApiServer);

    if (System.getenv(envApiServer) == null
        || System.getenv(envApiServer).isEmpty()
        || System.getenv(envApiServer).isBlank()) {
      logger.info(
          "System environment variable "
              + envApiServer
              + " not set. Using default value: "
              + apiUrlDefault);
      apiUrl = apiUrlDefault;
    } else {

      apiServer = System.getenv(envApiServer);

      if (apiServer.endsWith("/")) {
        apiServer = apiServer.substring(0, apiServer.length() - 1);
      }

      if (StringUtils.countOccurrencesOf(apiServer, "/") != 2) {
        logger.error(envApiServer + " from environment is invalid. (" + apiServerOriginal + ")");
        logger.error(envApiServer + ": using default value");
        apiServer = apiServerDefault;
      }

      apiUrl = apiServer + apiUrlPath;

      String[] schemes = {"http", "https"};
      UrlValidator urlValidator = new UrlValidator(schemes, ALLOW_LOCAL_URLS);

      if (!urlValidator.isValid(apiUrl)) {
        logger.error(
            "API URL"
                + " is invalid. (API_SERVER: "
                + apiServerOriginal
                + ", Derived Url: "
                + apiUrl
                + ")");
        logger.error("API URL: using default value");
        apiUrl = apiUrlDefault;
      }
    }

    logger.info("API URL: " + apiUrl);

    String path = getClass().getResource("/static/assets").getFile();
    File f = new File(path + "/config.json");
    logger.info(f.toString());

    FileWriter fw = new FileWriter(f.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write("{\n\"apiServer\":{\n\"url\":\"" + apiUrl + "\",\n\"urlAuth\":\"\"}\n}");
    bw.close();
  }
}
