package com.xueyi.modules.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * 监控权限配置
 *
 * @author xueyi
 */
@EnableWebSecurity
public class WebSecurityConfigurer {
    private final String adminContextPath;

    public WebSecurityConfigurer(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        return httpSecurity.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(adminContextPath + "/assets/**"
                        , adminContextPath + "/login"
                        , adminContextPath + "/actuator/**"
                        , adminContextPath + "/instances/**"
                ).permitAll().anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage(adminContextPath + "/login")
                        .successHandler(successHandler))
                .logout(logout -> logout.logoutUrl(adminContextPath + "/logout"))
                .httpBasic(httpBasic -> {
                })
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
