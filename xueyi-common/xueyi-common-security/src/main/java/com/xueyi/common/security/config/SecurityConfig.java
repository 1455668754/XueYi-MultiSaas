package com.xueyi.common.security.config;

import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.auth.AuthService;
import com.xueyi.common.security.filter.TokenAuthenticationFilter;
import com.xueyi.common.security.handler.AuthenticationLoseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全认证配置
 *
 * @author xueyi
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    protected RedisService redisService;

    @Autowired
    protected AuthenticationLoseHandler authenticationLoseHandler;

    @Autowired
    protected TokenAuthenticationFilter tokenAuthenticationFilter;

    /**
     * 权限验证
     */
    @Bean("ss")
    @ConditionalOnMissingBean(AuthService.class)
    public AuthService authService() {
        return new AuthService();
    }

    /**
     * 权限标识常量
     */
    @Bean("Auth")
    @ConditionalOnMissingBean(Auth.class)
    public Auth auth() {
        return new Auth();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF禁用，因为不使用session
        http.csrf().disable();
        // 禁用HTTP响应标头
        http.headers().cacheControl().disable();
        // 基于token，所以不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests(authorize -> authorize
                        // 静态资源，可匿名访问
//                        .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                        // 除上面外的所有请求全部需要鉴权认证
                        .anyRequest().authenticated()
        );
        // 添加JWT filter
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
        // 认证失败处理类
        http.exceptionHandling()
                .accessDeniedHandler(authenticationLoseHandler)
                .authenticationEntryPoint(authenticationLoseHandler);
        return http.build();
    }
}
