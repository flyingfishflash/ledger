package net.flyingfishflash.ledger.unit.core.multitenancy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.core.multitenancy.TenantConnectionProvider;
import net.flyingfishflash.ledger.core.multitenancy.TenantIdentifierResolver;

@ExtendWith(MockitoExtension.class)
class TenantConnectionProviderTests {

  @Mock Connection mockConnection;
  @Mock DataSource mockDataSource;
  @InjectMocks TenantConnectionProvider tenantConnectionProvider;

  @Test
  @DisplayName("getAnyConnection() Schema Changed to COMMON")
  void getAnyConnectionSchemaChangedToCommon() throws SQLException {
    given(mockDataSource.getConnection()).willReturn(mockConnection);
    tenantConnectionProvider.getAnyConnection();
    verify(mockConnection, times(1)).setSchema(TenantIdentifierResolver.COMMON);
    verifyNoMoreInteractions(mockConnection);
    assertThat(tenantConnectionProvider.getAnyConnection()).isInstanceOf(Connection.class);
  }

  @Test
  @DisplayName("releaseAnyConnection() Closes Provided Connection")
  void releaseAnyConnectionClosesProvidedConnection() throws SQLException {
    tenantConnectionProvider.releaseAnyConnection(mockConnection);
    verify(mockConnection, times(1)).close();
    verifyNoMoreInteractions(mockConnection);
  }

  @Test
  @DisplayName("getConnection() Schema Changed to tenantIdentifier When Not Equal to UNDEFINED")
  void getConnectionSchemaChangedToTenantIdentifierWhenNotEqualToUndefined() throws SQLException {
    var tenantIdentifier = "Lorem Ipsum";
    given(mockDataSource.getConnection()).willReturn(mockConnection);
    tenantConnectionProvider.getConnection(tenantIdentifier);
    verify(mockConnection, times(1)).setSchema(tenantIdentifier);
    verifyNoMoreInteractions(mockConnection);
    assertThat(tenantConnectionProvider.getConnection(tenantIdentifier))
        .isInstanceOf(Connection.class);
  }

  @Test
  @DisplayName("getConnection() Schema Not Changed When Equal to UNDEFINED")
  void getConnectionSchemaNotChangedWhenEqualToUndefined() throws SQLException {
    given(mockDataSource.getConnection()).willReturn(mockConnection);
    tenantConnectionProvider.getConnection(TenantIdentifierResolver.UNDEFINED);
    verifyNoInteractions(mockConnection);
  }

  @Test
  @DisplayName("releaseConnection() Schema Not Changed When Equal to UNDEFINED")
  void releaseConnectionSchemaNotChangedWhenEqualToUndefined() throws SQLException {
    tenantConnectionProvider.releaseConnection(TenantIdentifierResolver.UNDEFINED, mockConnection);
    verify(mockConnection, times(1)).close();
    verifyNoMoreInteractions(mockConnection);
  }

  @Test
  @DisplayName("releaseConnection() Schema Not Changed When Equal to COMMON")
  void releaseConnectionSchemaNotChangedWhenEqualToCommon() throws SQLException {
    tenantConnectionProvider.releaseConnection(TenantIdentifierResolver.COMMON, mockConnection);
    verify(mockConnection, times(1)).close();
    verifyNoMoreInteractions(mockConnection);
  }

  @Test
  @DisplayName("releaseConnection() Schema Changed To COMMON When Not Equal to COMMON/UNDEFINED")
  void releaseConnectionSchemaChangedToCommonWhenNotEqualToCommonOrUndefined() throws SQLException {
    var tenantIdentifier = "Lorem Ipsum";
    tenantConnectionProvider.releaseConnection(tenantIdentifier, mockConnection);
    verify(mockConnection, times(1)).setSchema(TenantIdentifierResolver.COMMON);
    verify(mockConnection, times(1)).close();
    verifyNoMoreInteractions(mockConnection);
  }

  @Test
  @DisplayName("supportsAggressiveRelease() Returns False")
  void supportsAggressiveReleaseReturnsFalse() {
    assertThat(tenantConnectionProvider.supportsAggressiveRelease()).isFalse();
  }

  @Test
  @DisplayName("isUnwrappableAs() Returns False")
  void isUnwrappableAsReturnsFalse() {
    assertThat(tenantConnectionProvider.isUnwrappableAs(Object.class)).isFalse();
  }

  @Test
  @DisplayName("unwrap() Returns Null")
  void unwrapReturnsNull() {
    assertThat(tenantConnectionProvider.unwrap(Object.class)).isNull();
  }
}
