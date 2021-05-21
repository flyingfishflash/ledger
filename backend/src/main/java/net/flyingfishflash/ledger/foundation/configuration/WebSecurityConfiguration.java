package net.flyingfishflash.ledger.foundation.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import net.flyingfishflash.ledger.foundation.authentication.CustomAuthenticationEntryPoint;
import net.flyingfishflash.ledger.foundation.authentication.CustomInvalidSessionStrategy;
import net.flyingfishflash.ledger.foundation.users.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration<S extends Session> extends WebSecurityConfigurerAdapter {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  private final UserService userService;

  private static final String[] AUTH_WHITELIST = {
    "/actuator/health",
    "/actuator/info",
    "/api/v1/ledger/auth/signin",
    "/api/v1/ledger/auth/signup",
    "/api/v1/ledger/actuator/info",
    "/assets/config.json",
    "/h2-console/**",
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/ws*"
  };

  private final FindByIndexNameSessionRepository<S> sessionRepository;

  @Autowired PasswordEncoder passwordEncoder;

  WebSecurityConfiguration(
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
      UserService userService,
      ObjectProvider<FindByIndexNameSessionRepository<S>> sessionRepository) {
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    this.userService = userService;
    this.sessionRepository = sessionRepository.getObject();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
  }

  @Bean
  public InvalidSessionStrategy invalidSessionStrategy() {
    return new CustomInvalidSessionStrategy();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http // .exceptionHandling()
        // .authenticationEntryPoint(customAuthenticationEntryPoint)
        // .and()
        .cors()
        .and()
        .httpBasic()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .and()
        .authorizeRequests()
        .antMatchers(AUTH_WHITELIST)
        .permitAll()
        .anyRequest()
        .authenticated();
    // .and()
    // .requestCache()
    // .requestCache(new NullRequestCache());

    http.sessionManagement()
        // .sessionFixation()
        // .migrateSession()
        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
        .invalidSessionStrategy(invalidSessionStrategy())
        // .invalidSessionUrl("/invalidSession.html")
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry());
    // .expiredUrl("/sessionExpired.html");

    // enable for h2 console
    // enable for non-production only
    http.csrf().disable();
    http.headers().frameOptions().disable();
    // #http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    http.headers()
        // .contentSecurityPolicy("script-src 'self'; report-to /csp-report-endpoint/")
        // .and()
        .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
        .and()
        // https://w3c.github.io/permissions/#permission-registry
        .permissionsPolicy(
            permissions ->
                permissions.policy(
                    "geolocation=(none) accelerometer=(none) camera=(none) microphone=(none"));
    // @formatter:on
  }
}
