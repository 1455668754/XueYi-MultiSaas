package com.xueyi.auth.support.core;

import com.xueyi.auth.support.handler.FormAuthenticationFailureHandler;
import com.xueyi.auth.support.handler.SsoLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * 统一认证登录 - 授权码模式
 *
 * @author xueyi
 */
public final class FormIdentityLoginConfigurer extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> {
                    formLogin.loginPage("/token/login");
                    formLogin.loginProcessingUrl("/token/form");
                    formLogin.failureHandler(new FormAuthenticationFailureHandler());
                }).logout() // SSO登出成功处理
                .logoutSuccessHandler(new SsoLogoutSuccessHandler()).deleteCookies("JSESSIONID")
                .invalidateHttpSession(true).and().csrf().disable();
    }

}
