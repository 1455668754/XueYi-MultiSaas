package com.xueyi.auth.support.base;

import com.xueyi.auth.login.base.IUserDetailsService;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.error.OAuth2Constants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.security.Principal;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 自定义授权处理器
 *
 * @author xueyi
 */
@Slf4j
public abstract class AuthenticationBaseProvider<T extends AuthenticationBaseToken> implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    @Deprecated
    private Supplier<String> refreshTokenGenerator;

    public AuthenticationBaseProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Deprecated
    public void setRefreshTokenGenerator(Supplier<String> refreshTokenGenerator) {
        Assert.notNull(refreshTokenGenerator, "refreshTokenGenerator cannot be null");
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    /**
     * 构建登录验证信息体
     *
     * @param reqParameters 请求参数
     * @return 登录验证信息体
     */
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        HttpServletRequest request = ServletUtil.getRequest();
        if (ObjectUtil.isNull(request))
            throw new InternalAuthenticationServiceException("web request is empty");

        String grantType = request.getParameter(SecurityConstants.OAuth2ParameterNames.GRANT_TYPE.getCode());
        String accountType = request.getParameter(SecurityConstants.OAuth2ParameterNames.ACCOUNT_TYPE.getCode());

        Map<String, IUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(IUserDetailsService.class);

        Optional<IUserDetailsService> optional = userDetailsServiceMap.values().stream()
                .filter(service -> service.support(grantType, accountType))
                .max(Comparator.comparingInt(Ordered::getOrder));

        if (optional.isEmpty()) {
            throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
        }
        return optional.get().buildToken(reqParameters);
    }

    /**
     * 校验是否支持此令牌类型
     */
    @Override
    public abstract boolean supports(Class<?> authentication);

    /**
     * 当前的请求客户端是否支持此模式
     */
    public abstract void checkClient(RegisteredClient registeredClient);

    /**
     * 认证校验
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        T baseAuthentication = (T) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(baseAuthentication);

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        checkClient(registeredClient);

        Set<String> authorizedScopes;
        // Default to configured scopes
        if (CollUtil.isNotEmpty(baseAuthentication.getScopes())) {
            Assert.notNull(registeredClient, "registeredClient cannot be null");
            for (String requestedScope : baseAuthentication.getScopes()) {
                if (!CollUtil.contains(registeredClient.getScopes(), requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(baseAuthentication.getScopes());
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.SCOPE_IS_EMPTY.getCode()));
        }

        Map<String, Object> reqParameters = baseAuthentication.getAdditionalParameters();
        try {

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(reqParameters);

            log.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);

            Authentication usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // @formatter:off
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder().registeredClient(registeredClient).principal(usernamePasswordAuthentication).authorizationServerContext(AuthorizationServerContextHolder.getContext()).authorizedScopes(authorizedScopes).authorizationGrantType(AuthorizationGrantType.PASSWORD).authorizationGrant(baseAuthentication);
            // @formatter:on
            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient).principalName(usernamePasswordAuthentication.getName()).authorizationGrantType(AuthorizationGrantType.PASSWORD).authorizedScopes(authorizedScopes);

            // ----- Access token -----
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            if (generatedAccessToken == null) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the access token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(), generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
            if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
                authorizationBuilder.id(accessToken.getTokenValue()).token(accessToken, (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims())).authorizedScopes(authorizedScopes).attribute(Principal.class.getName(), usernamePasswordAuthentication);
            } else {
                authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
            }

            // ----- Refresh token -----
            OAuth2RefreshToken refreshToken = null;
            if (CollUtil.contains(registeredClient.getAuthorizationGrantTypes(), AuthorizationGrantType.REFRESH_TOKEN)
                    // Do not issue refresh token to public client
                    && ObjectUtil.notEqual(ClientAuthenticationMethod.NONE, clientPrincipal.getClientAuthenticationMethod())) {

                if (this.refreshTokenGenerator != null) {
                    Instant issuedAt = Instant.now();
                    Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
                    refreshToken = new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt, expiresAt);
                } else {
                    tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                    OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                    if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the refresh token.", ERROR_URI);
                        throw new OAuth2AuthenticationException(error);
                    }
                    refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                }
                authorizationBuilder.refreshToken(refreshToken);
            }

            OAuth2Authorization authorization = authorizationBuilder.build();

            this.authorizationService.save(authorization);

            log.debug("returning OAuth2AccessTokenAuthenticationToken");
            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));

        } catch (Exception ex) {
            log.error("problem in authenticate", ex);
            throw oAuth2AuthenticationException((AuthenticationException) ex);
        }
    }

    /**
     * 登录异常转换为oauth2异常
     *
     * @param authenticationException 身份验证异常
     * @return {@link OAuth2AuthenticationException}
     */
    private OAuth2AuthenticationException oAuth2AuthenticationException(AuthenticationException authenticationException) {
        if (authenticationException instanceof UsernameNotFoundException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.USERNAME_NOT_FOUND.getCode(), authenticationException.getLocalizedMessage(), StrUtil.EMPTY));
        if (authenticationException instanceof BadCredentialsException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.BAD_CREDENTIALS.getCode(), authenticationException.getLocalizedMessage(), StrUtil.EMPTY));
        if (authenticationException instanceof LockedException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.USER_LOCKED.getCode(), OAuth2Constants.Error.USER_LOCKED.getMsg(), StrUtil.EMPTY));
        if (authenticationException instanceof DisabledException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.USER_DISABLE.getCode(), OAuth2Constants.Error.USER_DISABLE.getMsg(), StrUtil.EMPTY));
        if (authenticationException instanceof AccountExpiredException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.USER_EXPIRED.getCode(), OAuth2Constants.Error.USER_EXPIRED.getMsg(), StrUtil.EMPTY));
        if (authenticationException instanceof CredentialsExpiredException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.CREDENTIALS_EXPIRED.getCode(), OAuth2Constants.Error.CREDENTIALS_EXPIRED.getMsg(), StrUtil.EMPTY));
        if (authenticationException instanceof OAuth2AuthenticationException)
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2Constants.Error.INVALID_SCOPE.getCode(), OAuth2Constants.Error.INVALID_SCOPE.getMsg(), StrUtil.EMPTY));
        return new OAuth2AuthenticationException(OAuth2Constants.Error.UN_KNOW_LOGIN_ERROR.getCode());
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass()))
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        if (clientPrincipal != null && clientPrincipal.isAuthenticated())
            return clientPrincipal;
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
