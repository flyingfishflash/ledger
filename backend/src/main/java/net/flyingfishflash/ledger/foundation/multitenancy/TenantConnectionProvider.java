package net.flyingfishflash.ledger.foundation.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

  @SuppressWarnings("java:S1948")
  private DataSource datasource;

  public TenantConnectionProvider(DataSource datasource) {
    this.datasource = datasource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return datasource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @SuppressWarnings("java:S2095")
  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    final var connection = getAnyConnection();
    connection.createStatement().execute(String.format("SET search_path TO %s", tenantIdentifier));
    return connection;
  }

  @SuppressWarnings("java:S2095")
  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection)
      throws SQLException {
    connection
        .createStatement()
        .execute(String.format("SET search_path TO %s", TenantIdentifierResolver.COMMON));
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public boolean isUnwrappableAs(Class unwrapType) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    return null;
  }
}
