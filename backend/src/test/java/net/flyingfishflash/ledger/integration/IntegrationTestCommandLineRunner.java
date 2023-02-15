// package net.flyingfishflash.ledger.integration;
//
// import javax.sql.DataSource;
//
// import org.flywaydb.core.Flyway;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
//
// @Component
// public class IntegrationTestCommandLineRunner implements CommandLineRunner {
//
//  private final DataSource dataSource;
//
//  public IntegrationTestCommandLineRunner(DataSource dataSource) {
//    this.dataSource = dataSource;
//  }
//
//  @Override
//  public void run(String... args) throws Exception {
//    Flyway flyway =
//        Flyway.configure()
//            .locations("db/migration/users")
//            .dataSource(dataSource)
//           //.schemas("testuser")
//            .load();
//    flyway.migrate();
//  }
// }
