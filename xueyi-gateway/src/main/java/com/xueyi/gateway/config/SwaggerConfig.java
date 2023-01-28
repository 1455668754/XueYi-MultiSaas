package com.xueyi.gateway.config;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.HashSet;
import java.util.Set;

/**
 * 聚合系统接口
 *
 * @author xueyi
 */
@Component
public class SwaggerConfig implements WebFluxConfigurer {

    /** Swagger3默认的url后缀 */
    public static final String SWAGGER3URL = "/v3/api-docs";

    @Autowired
    private GatewayProperties gatewayProperties;

    /**
     * 聚合其他服务接口
     */
    @Bean
    public SwaggerUiConfigProperties swaggerUiConfig(SwaggerUiConfigProperties config) {
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls = new HashSet<>();
        gatewayProperties.getRoutes()
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                        .filter(predicateDefinition -> !"xueyi-auth".equalsIgnoreCase(routeDefinition.getId()))
                        .forEach(predicateDefinition -> {
                            String url = predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", SWAGGER3URL);
                            swaggerUrls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(routeDefinition.getId(), url, routeDefinition.getId()));
                        }));
        config.setUrls(swaggerUrls);
        return config;
    }
}