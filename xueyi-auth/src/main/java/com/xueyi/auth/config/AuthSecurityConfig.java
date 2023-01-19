package com.xueyi.auth.config;

import com.xueyi.auth.provider.XueAuthenticationProvider;
import com.xueyi.auth.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

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
}