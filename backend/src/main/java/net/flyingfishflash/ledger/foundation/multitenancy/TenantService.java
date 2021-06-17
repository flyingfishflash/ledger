package net.flyingfishflash.ledger.foundation.multitenancy;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class TenantService {

  private final DataSource dataSource;

  public TenantService(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void initDatabase(String schema) {
    var flyway =
        Flyway.configure()
            .locations("db/migration/" + TenantIdentifierResolver.USERS)
            .dataSource(dataSource)
            .schemas(schema)
            .load();
    flyway.migrate();
  }
}
