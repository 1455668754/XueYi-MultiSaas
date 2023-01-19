package com.xueyi.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全认证配置
 *
 * @author xueyi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** 不需要拦截地址 */
    public static final String[] excludeUrls = {"/login", "/logout", "/refresh"};

    /** 不需要拦截地址 */
    public static final String[] excludeResources = {"/static/**", "/resources/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO 调整校验 - 校验是否存在内链标识 - 切换拦截器
        return http.authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .requestMatchers(excludeUrls).permitAll()
                                .requestMatchers(excludeResources).permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                                .and().csrf(AbstractHttpConfigurer::disable);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        ).build();
    }
}
