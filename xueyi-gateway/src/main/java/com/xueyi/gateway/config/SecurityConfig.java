package com.xueyi.gateway.config;


import com.xueyi.gateway.config.properties.IgnoreWhiteProperties;
import com.xueyi.gateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全认证配置
 *
 * @author xueyi
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {

    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private AuthFilter authFilter;

    /** 不需要拦截地址 */
    public static final String[] excludeResources = {"/static/**", "/resources/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO 调整校验 - 校验是否存在内链标识 - 切换拦截器
        return http.authorizeHttpRequests(authorize -> {
                            try {
                                authorize
                                        .requestMatchers(ignoreWhite.getWhites()).permitAll()
                                        .requestMatchers(excludeResources).permitAll()
                                        .requestMatchers("/**").permitAll()
                                        .anyRequest().authenticated()
                                        .and().csrf(AbstractHttpConfigurer::disable);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}

