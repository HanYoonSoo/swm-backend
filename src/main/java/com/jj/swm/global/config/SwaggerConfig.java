package com.jj.swm.global.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    static {
        io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
    }

    @Bean
    public GroupedOpenApi v1OpenAPI() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(openApi -> openApi.info(v1Info()))
                .build();
    }

    private Info v1Info() {
        return new Info()
                .title("Study With Me API")
                .version("1.0")
                .description("API version 1 documentation");
    }
}
