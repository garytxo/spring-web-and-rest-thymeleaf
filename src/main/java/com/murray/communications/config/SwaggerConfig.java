package com.murray.communications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the application web configuration such as:
 * <ul>
 * <li>Registering the Spring Converters</li>
 *
 * <li>Swagger configuration</li>
 * </ul>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.murray.communications.controllers.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
    @Bean
    public SecurityConfiguration securityInfo() {

        Map<String, Object> maps = new HashMap<>();
        maps.put(ApiKeyVehicle.HEADER.getValue(), HttpHeaders.AUTHORIZATION);

        return SecurityConfigurationBuilder.builder()
                .additionalQueryStringParams(maps)
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "COMMUNICATIONS REST API",
                "REST API of communication services",
                "v1",
                "",
                new Contact("", "", ""),
                "",
                "",
                new ArrayList<>());
    }

}
