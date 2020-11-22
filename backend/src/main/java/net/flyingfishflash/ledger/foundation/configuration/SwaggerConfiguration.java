package net.flyingfishflash.ledger.foundation.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfiguration {

  @Bean
  public Docket api() {
    System.out.println("Swagger!");
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("net.flyingfishflash.ledger"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .securityContexts(Arrays.asList(securityContext()))
        .securitySchemes(Arrays.asList(basicAuthScheme()));
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Ledger REST API")
        // .description("Some custom description of API.")
        .license("MIT License")
        .licenseUrl("https://opensource.org/licenses/MIT")
        .version("1.0.0")
        .build();
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(Arrays.asList(basicAuthReference()))
        .build();
  }

  private SecurityScheme basicAuthScheme() {
    return new BasicAuth("basicAuth");
  }

  private SecurityReference basicAuthReference() {
    return new SecurityReference("basicAuth", new AuthorizationScope[0]);
  }
}
