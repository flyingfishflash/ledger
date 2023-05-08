package net.flyingfishflash.ledger.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import net.flyingfishflash.ledger.core.authentication.CustomAuthenticationEntryPoint;
import net.flyingfishflash.ledger.core.authentication.CustomInvalidSessionStrategy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  WebSecurityConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
  }

  @Bean
  public InvalidSessionStrategy invalidSessionStrategy() {
    return new CustomInvalidSessionStrategy();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .cors()
        .and()
        .csrf()
        .disable()
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        .httpBasic()
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .and()
        .authorizeHttpRequests()
        .requestMatchers(
            AntPathRequestMatcher.antMatcher("/api/v1/auth/signin"),
            AntPathRequestMatcher.antMatcher("/api/v1/auth/signup"),
            AntPathRequestMatcher.antMatcher("/api/v1/health"),
            AntPathRequestMatcher.antMatcher("/api/v1/info"),
            AntPathRequestMatcher.antMatcher("/h2-console"),
            AntPathRequestMatcher.antMatcher("/h2-console/**"),
            AntPathRequestMatcher.antMatcher("/swagger-ui.html"),
            AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
            AntPathRequestMatcher.antMatcher("/v3/api-docs"),
            AntPathRequestMatcher.antMatcher("/v3/api-docs/**"))
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        .invalidSessionStrategy(invalidSessionStrategy())
        .and()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .headers()
        .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
        .and()
        // https://w3c.github.io/permissions/#permission-registry
        .permissionsPolicy(
            permissions ->
                permissions.policy(
                    "geolocation=(none) accelerometer=(none) camera=(none) microphone=(none"));

    return httpSecurity.build();
  }
}
