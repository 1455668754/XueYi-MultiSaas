package com.xueyi.common.security.config;

import cn.hutool.core.util.ArrayUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.config.properties.PermitAllUrlProperties;
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

    /** token认证过滤器 */
    @Autowired
    protected TokenAuthenticationFilter tokenAuthenticationFilter;

    /** 允许匿名访问的地址 */
    @Autowired
    protected PermitAllUrlProperties permitAllUrlProperties;

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
                // 注解标记允许匿名访问的url
                .requestMatchers(ArrayUtil.toArray(permitAllUrlProperties.getUrls(), String.class)).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
        );
        // 添加JWT filter
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
        // 认证失败处理类
        http.exceptionHandling().authenticationEntryPoint(authenticationLoseHandler);
        return http.build();
    }
}
