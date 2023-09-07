package net.flyingfishflash.ledger.core.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("*"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    configuration.setExposedHeaders(List.of("X-Auth-Token", "Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public InvalidSessionStrategy invalidSessionStrategy() {
    return new CustomInvalidSessionStrategy();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .cors(withDefaults())
        // -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(basic -> basic.authenticationEntryPoint(customAuthenticationEntryPoint))
        .authorizeHttpRequests(
            authentication ->
                authentication
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
                    .authenticated())
        .sessionManagement(
            sessions ->
                sessions
                    // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionStrategy(invalidSessionStrategy()))
        .headers(
            headers ->
                headers
                    .referrerPolicy(
                        referrer ->
                            referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
                    .frameOptions(withDefaults())
                    .disable())
        .headers(
            headers ->
                // https://w3c.github.io/permissions/#permission-registry
                headers.permissionsPolicy(
                    permissions ->
                        permissions.policy(
                            "geolocation=(none) accelerometer=(none) camera=(none) microphone=(none")));
    return httpSecurity.build();
  }
}
