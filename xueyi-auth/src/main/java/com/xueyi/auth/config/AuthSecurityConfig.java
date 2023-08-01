package com.xueyi.auth.config;

import com.xueyi.auth.handler.AuthenticationProvider;
import com.xueyi.auth.handler.FormIdentityLoginConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                        .anyRequest().authenticated()
                )
                // 避免iframe同源无法登录
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 表单登录个性化
                .apply(new FormIdentityLoginConfigurer());
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new AuthenticationProvider());
        return http.build();
    }
}