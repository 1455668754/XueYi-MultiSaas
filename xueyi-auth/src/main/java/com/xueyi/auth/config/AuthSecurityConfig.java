package com.xueyi.auth.config;

import com.xueyi.auth.provider.XueAuthenticationProvider;
import com.xueyi.auth.service.IUserDetailsService;
import com.xueyi.auth.service.impl.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
public class AuthSecurityConfig {

    @Autowired
    private IUserDetailsService userDetailsService;

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public XueAuthenticationProvider customerDaoAuthenticationProvider() {
        XueAuthenticationProvider authenticationProvider = new XueAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler)
                .and().csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}