package com.xueyi.auth.config;

import com.xueyi.auth.handler.AuthenticationProvider;
import com.xueyi.auth.handler.FormIdentityLoginConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全认证配置
 *
 * @author xueyi
 */
@EnableWebSecurity
public class AuthSecurityConfig {

    /**
     * 安全策略
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 开放自定义的部分端点
                        .requestMatchers("/*").permitAll()
                        // 避免iframe同源无法登录
                        .anyRequest().authenticated()
                ).headers().frameOptions().sameOrigin()
                // 表单登录个性化
                .and().apply(new FormIdentityLoginConfigurer());
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new AuthenticationProvider());
        return http.build();
    }

    /**
     * 暴露静态资源
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http.securityMatchers((matchers) -> matchers.requestMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll()).requestCache().disable()
                .securityContext().disable().sessionManagement().disable();
        return http.build();
    }
}