package com.xueyi.common.security.config;

import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.security.config.properties.PermitAllUrlProperties;
import com.xueyi.common.security.handler.BearerTokenHandler;
import com.xueyi.common.security.handler.ResourceAuthenticationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 * 安全认证配置
 *
 * @author xueyi
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ResourceAuthenticationHandler resourceAuthenticationHandler;

    private final PermitAllUrlProperties permitAllUrl;

    private final BearerTokenHandler bearerTokenHandler;

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> {
                    permitAllUrl.getCustom().keySet().forEach(item -> {
                        HttpMethod httpMethod = HttpMethod.valueOf(item.name());
                        List<String> list = permitAllUrl.getCustom().get(item);
                        authorizeRequests.requestMatchers(httpMethod, ArrayUtil.toArray(list, String.class)).permitAll();
                    });
                    authorizeRequests
                            // 注解标记允许匿名访问的url
                            .requestMatchers(ArrayUtil.toArray(permitAllUrl.getRoutine(), String.class)).permitAll()
                            // 除上面外的所有请求全部需要鉴权认证
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(
                        oauth2 -> oauth2.opaqueToken(token -> token
                                        .introspector(opaqueTokenIntrospector)
                                )
                                // 认证失败处理类
                                .authenticationEntryPoint(resourceAuthenticationHandler)
                                .bearerTokenResolver(bearerTokenHandler))
                // 禁用HTTP响应标头
                .headers().frameOptions().disable()
                // CSRF禁用，因为不使用session
                .and().csrf().disable();
        return http.build();
    }
}
