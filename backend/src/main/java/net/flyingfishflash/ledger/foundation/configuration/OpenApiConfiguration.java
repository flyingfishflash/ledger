package net.flyingfishflash.ledger.foundation.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI springShopOpenAPI() {
    final var securitySchemeName = "basicAuth";
    return new OpenAPI()
        .info(
            new Info()
                .title("Ledger REST API")
                // .description("API Description")
                .version("v1.0.0")
                .license(new License().name("MIT License").url("https://mit-license.org/")))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .addServersItem(new Server().url("/"))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")))
        .externalDocs(
            new ExternalDocumentation()
                .description("Ledger Wiki Documentation")
                .url("https://ledger.wiki.github.org/docs"));
  }
}
