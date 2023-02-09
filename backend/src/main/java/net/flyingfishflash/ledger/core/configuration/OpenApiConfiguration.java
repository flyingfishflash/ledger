package net.flyingfishflash.ledger.core.configuration;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
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

  @Bean
  public OpenApiCustomiser sortTagsAlphabetically() {
    return openApi ->
        openApi.setTags(
            openApi.getTags().stream()
                .sorted(Comparator.comparing(tag -> StringUtils.stripAccents(tag.getName())))
                .toList());
  }

  @SuppressWarnings("java:S3740")
  @Bean
  public OpenApiCustomiser sortSchemasAlphabetically() {
    return openApi -> {
      Map<String, Schema> schemas = openApi.getComponents().getSchemas();
      openApi.getComponents().setSchemas(new TreeMap<>(schemas));
    };
  }
}
