package com.xueyi.gateway.config;


import com.xueyi.common.redis.service.RedisService;
import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import com.xueyi.gateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 安全认证配置
 *
 * @author xueyi
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange((authorize) -> authorize.anyExchange().permitAll())
                // 网关鉴权
                .addFilterAfter(new AuthFilter(redisService, ignoreWhite), SecurityWebFiltersOrder.FIRST)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .headers(headerSpec -> headerSpec.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                // CSRF禁用，因为不使用session
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }
}
