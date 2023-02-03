package com.xueyi.auth.config;

import com.xueyi.auth.service.impl.AuthenticationEventHandlerImpl;
import com.xueyi.auth.support.core.AuthenticationProvider;
import com.xueyi.auth.support.core.CustomTokenCustomizer;
import com.xueyi.auth.support.core.CustomTokenGenerator;
import com.xueyi.auth.support.core.FormIdentityLoginConfigurer;
import com.xueyi.auth.support.password.AuthenticationPasswordConverter;
import com.xueyi.auth.support.password.AuthenticationPasswordProvider;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;

/**
 * 认证服务器配置
 *
 * @author xueyi
 */
@Configuration
public class AuthServerConfig {

    @Autowired
    private OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.apply(authorizationServerConfigurer
                // 个性化认证授权端点
                .tokenEndpoint((tokenEndpoint) -> tokenEndpoint
                        // 注入自定义的授权认证Converter
                        .accessTokenRequestConverter(accessTokenRequestConverter())
                        // 登录成功处理器
                        .accessTokenResponseHandler(new AuthenticationEventHandlerImpl())
                        // 登录失败处理器
                        .errorResponseHandler(new AuthenticationEventHandlerImpl())
                )
                // 个性化客户端认证
                .clientAuthentication(oAuth2ClientAuthenticationConfigurer -> oAuth2ClientAuthenticationConfigurer
                        // 处理客户端认证异常
                        .errorResponseHandler(new AuthenticationEventHandlerImpl())
                )
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        // 授权码端点个性化confirm页面
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)
                )
        );

        DefaultSecurityFilterChain securityFilterChain = http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 自定义接口、端点暴露
                        .requestMatchers("/token/**", "/actuator/**", "/css/**", "/error").permitAll()
                        .anyRequest().authenticated()
                ).apply(authorizationServerConfigurer
                        // redis存储token的实现
                        .authorizationService(authorizationService)
                        .authorizationServerSettings(AuthorizationServerSettings.builder().issuer(SecurityConstants.PROJECT_LICENSE).build()))
                // 授权码登录的登录页个性化
                .and().apply(new FormIdentityLoginConfigurer()).and().build();

        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }

    /**
     * 令牌生成规则实现
     */
    @Bean
    @SuppressWarnings("rawtypes")
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        CustomTokenGenerator accessTokenGenerator = new CustomTokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomTokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * Token注入请求转换器
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new AuthenticationPasswordConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }

    /**
     * 自定义授权模式
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        // 密码模式
        AuthenticationPasswordProvider authenticationPasswordProvider = new AuthenticationPasswordProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        http.authenticationProvider(new AuthenticationProvider());
        http.authenticationProvider(authenticationPasswordProvider);
    }
}
