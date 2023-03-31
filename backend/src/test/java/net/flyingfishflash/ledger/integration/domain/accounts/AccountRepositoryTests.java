// package net.flyingfishflash.ledger.integration.domain.accounts;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import java.sql.SQLException;
// import java.util.Optional;
//
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.transaction.annotation.Transactional;
//
// import net.flyingfishflash.ledger.domain.accounts.data.Account;
// import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
// import net.flyingfishflash.ledger.domain.accounts.data.AccountRepository;
// import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
//
// import javax.sql.DataSource;
//
// @SpringBootTest
// @Transactional
// @WithMockUser("testuser")
//// @ExtendWith(MockitoExtension.class)
// class AccountRepositoryTests {
//
//  //  @TestConfiguration
//  //  static class AdditionalCfg {
//  //    // @Primary
//  //    @Bean
//  //    TenantIdentifierResolver tenantIdentifierResolver() {
//  //      return new TenantIdentifierResolver(securityContextService) {
//  //        @Override
//  //        public String resolveCurrentTenantIdentifier() {
//  //          return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//  //              .filter(Predicate.not(AnonymousAuthenticationToken.class::isInstance))
//  //              .map(Principal::getName)
//  //              .orElse(USERS);
//  //        }
//  //      };
//  //    }
//  //  }
//
//  // @MockBean private SecurityContextService securityContextService;
//  //  @SpyBean SecurityContextService securityContextService;
//  //  @SpyBean TenantIdentifierResolver tenantIdentifierResolver;
//  @Autowired private AccountRepository accountRepository;
//  @Autowired private DataSource dataSource;
//
//  //  @BeforeEach
//  //  void setAuth() {
//  //    Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//  //
//  //    Authentication auth = mock(Authentication.class);
//  //    // when(auth.getPrincipal()).thenReturn(principal);
//  //    SecurityContext securityContext = mock(SecurityContext.class);
//  //    when(securityContext.getAuthentication()).thenReturn(auth);
//  //    when(auth.getName()).thenReturn("aName");
//  //    SecurityContextHolder.setContext(securityContext);
//  //  }
//
//  @Test
//  void findRoot() throws SQLException {
//
//    System.out.println("starting test");
//
//    //    var user = new User();
//    //    user.setUsername("test");
//    //    var userList = List.of(user);
//    //    when(mockUserRepository.findAll()).thenReturn(userList);
//    Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//    //    when(securityContextService.getAuthentication()).thenReturn(principal);
//
//    // var resolver = tenantIdentifierResolver.resolveCurrentTenantIdentifier();
//
//    // when(securityContextService.getAuthentication()).thenReturn(principal);
//
//    // dataSource.getConnection().setSchema("testuser");
//    //    var blah = doReturn(principal).when(securityContextService.getAuthentication());
//
//    //    when(securityContextService.getAuthentication()).thenReturn(principal);
//    // System.out.println(resolver);
//
//    // when(tenantIdentifierResolver.resolveCurrentTenantIdentifier()).thenReturn("testuser");
//
//    var connection = dataSource.getConnection();
//    connection.setSchema("testuser");
//
//    Optional<Account> account = accountRepository.findRoot();
//    assertThat(account).isPresent();
//    assertThat(account.get())
//        .hasFieldOrPropertyWithValue("parentId", null)
//        .hasFieldOrPropertyWithValue("category", AccountCategory.ROOT)
//        .hasFieldOrPropertyWithValue("type", AccountType.ROOT);
//  }
//
//  @Test
//  void findById() {
//    var id = 32L;
//    Optional<Account> account = accountRepository.findById(id);
//    assertThat(account).isPresent();
//    assertThat(account.get()).hasFieldOrPropertyWithValue("id", id);
//  }
//
//  @Test
//  void findByGuid() {
//    var guid = "ea2c06e9dd20e09797b025d24deca332";
//    Optional<Account> account = accountRepository.findByGuid(guid);
//    assertThat(account).isPresent();
//    assertThat(account.get()).hasFieldOrPropertyWithValue("guid", guid);
//  }
//
//  @Test
//  void rootLevelNodeCount() {
//    var id = 32L;
//    Optional<Account> account = accountRepository.findById(id);
//    assertThat(account).isPresent();
//    assertThat(accountRepository.rootLevelNodeCount(account.get())).isEqualTo(1L);
//  }
// }
