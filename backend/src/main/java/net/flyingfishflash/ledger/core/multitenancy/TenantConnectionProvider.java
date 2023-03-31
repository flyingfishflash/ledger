package net.flyingfishflash.ledger.core.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TenantConnectionProvider
    implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

  @SuppressWarnings("java:S1948")
  private final DataSource datasource;

  public TenantConnectionProvider(DataSource datasource) {
    this.datasource = datasource;
  }

  @Override
  @SuppressWarnings("java:S2095")
  public Connection getAnyConnection() throws SQLException {
    var connection = datasource.getConnection();
    connection.setSchema(TenantIdentifierResolver.COMMON);
    return connection;
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  @SuppressWarnings("java:S2095")
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    var connection = datasource.getConnection();
    if (!tenantIdentifier.equals(TenantIdentifierResolver.UNDEFINED)) {
      connection.setSchema(tenantIdentifier);
    }
    return connection;
  }

  @SuppressWarnings("java:S2095")
  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection)
      throws SQLException {
    if (!(tenantIdentifier.equals(TenantIdentifierResolver.UNDEFINED)
        || tenantIdentifier.equals(TenantIdentifierResolver.COMMON))) {
      connection.setSchema(TenantIdentifierResolver.COMMON);
    }
    connection.close();
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

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
  }
}
