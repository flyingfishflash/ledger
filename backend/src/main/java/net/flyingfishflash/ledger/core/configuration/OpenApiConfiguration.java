package net.flyingfishflash.ledger.core.configuration;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

import java.util.Map;
import java.util.TreeMap;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.Schema;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations

@OpenAPIDefinition(
    info =
        @Info(
            title = "${config.application.name}",
            version = "1.0.0",
            description = "${config.application.description}"),
    security = @SecurityRequirement(name = "basicAuthentication"),
    servers = {@Server(description = "server 1", url = "/")})
@SecurityScheme(name = "basicAuthentication", type = HTTP, scheme = "basic")
@Configuration
public class OpenApiConfiguration {

  @SuppressWarnings("java:S3740")
  @Bean
  public OpenApiCustomizer sortSchemasAlphabetically() {
    return openApi -> {
      Map<String, Schema> schemas = openApi.getComponents().getSchemas();
      openApi.getComponents().setSchemas(new TreeMap<>(schemas));
    };
  }
}
