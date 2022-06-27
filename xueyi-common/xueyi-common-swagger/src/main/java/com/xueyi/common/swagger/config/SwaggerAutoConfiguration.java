package com.xueyi.common.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(apiInfo(swaggerProperties()))
                .components(components());
    }

    private Info apiInfo(SwaggerProperties swaggerProperties) {
        return new Info()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(license(swaggerProperties))
                .contact(contact(swaggerProperties))
                .version(swaggerProperties.getVersion());
    }

    private License license(SwaggerProperties swaggerProperties) {
        return new License()
                .name(swaggerProperties.getLicense())
                .url(swaggerProperties.getLicenseUrl());
    }

    private Contact contact(SwaggerProperties swaggerProperties) {
        return new Contact()
                .name(swaggerProperties.getContact().getName())
                .url(swaggerProperties.getContact().getUrl())
                .email(swaggerProperties.getContact().getEmail());
    }

    /**
     * 默认的全局鉴权策略
     */
    private Components components() {
        return new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Authorization").bearerFormat("JWT"));
    }
}