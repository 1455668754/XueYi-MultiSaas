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

import java.util.Optional;

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
                .title(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getTitle).orElse(null))
                .description(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getDescription).orElse(null))
                .license(license(swaggerProperties))
                .contact(contact(swaggerProperties))
                .termsOfService(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getTermsOfService).orElse(null))
                .version(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getVersion).orElse(null));
    }

    private License license(SwaggerProperties swaggerProperties) {
        return new License()
                .name(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getLicense).map(SwaggerProperties.License::getName).orElse(null))
                .url(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getLicense).map(SwaggerProperties.License::getUrl).orElse(null));
    }

    private Contact contact(SwaggerProperties swaggerProperties) {
        return new Contact()
                .name(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getContact).map(SwaggerProperties.Contact::getName).orElse(null))
                .url(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getContact).map(SwaggerProperties.Contact::getUrl).orElse(null))
                .email(Optional.ofNullable(swaggerProperties).map(SwaggerProperties::getInfo).map(SwaggerProperties.InfoProperties::getContact).map(SwaggerProperties.Contact::getEmail).orElse(null));
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
