package com.xueyi.common.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Swagger文档配置
 *
 * @author Vondser
 */
@AutoConfiguration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerAutoConfiguration {

    @Bean
    public OpenAPI springOpenAPI(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(apiInfo(swaggerProperties))
                .components(components());
    }

    private Info apiInfo(SwaggerProperties swaggerProperties) {
        return new Info()
                .title(swaggerProperties.getInfo().getTitle())
                .description(swaggerProperties.getInfo().getDescription())
                .license(license(swaggerProperties))
                .contact(contact(swaggerProperties))
                .termsOfService(swaggerProperties.getInfo().getTermsOfService())
                .version(swaggerProperties.getInfo().getVersion());
    }

    private License license(SwaggerProperties swaggerProperties) {
        return new License()
                .name(swaggerProperties.getInfo().getLicense().getName())
                .url(swaggerProperties.getInfo().getLicense().getUrl());
    }

    private Contact contact(SwaggerProperties swaggerProperties) {
        return new Contact()
                .name(swaggerProperties.getInfo().getContact().getName())
                .url(swaggerProperties.getInfo().getContact().getName())
                .email(swaggerProperties.getInfo().getContact().getEmail());
    }

    /**
     * 默认的全局鉴权策略
     */
    private Components components() {
        return new Components()
                .addSecuritySchemes("token", new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                        .bearerFormat("JWT"));
    }

    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder().group("all")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList("token"));
                    return operation;
                }).build();
    }
}
