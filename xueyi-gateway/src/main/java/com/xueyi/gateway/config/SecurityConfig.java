package com.xueyi.gateway.config;


import com.xueyi.common.redis.service.RedisService;
import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import com.xueyi.gateway.filter.AuthFilter;
import com.xueyi.gateway.handler.AuthenticationLoseHandler;
import com.xueyi.gateway.handler.SecurityContextHandler;
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

    /** 不需要拦截地址 */
    public static final String[] excludeResources = {"/code"};

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authority -> authority
                        .pathMatchers(ignoreWhite.getWhites()).permitAll()
                        .pathMatchers("/code").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAfter(new AuthFilter(redisService, ignoreWhite), SecurityWebFiltersOrder.FIRST)
                .securityContextRepository(new SecurityContextHandler(redisService, ignoreWhite))
                .exceptionHandling().authenticationEntryPoint(new AuthenticationLoseHandler())
                .and().cors().disable().csrf().disable();
        return http.build();
    }
}

