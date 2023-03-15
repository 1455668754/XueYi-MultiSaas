package com.xueyi.common.security.handler;

import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.BooleanUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.feign.RemoteClientService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * 客户端认证 服务层处理
 *
 * @author xueyi
 */
@RequiredArgsConstructor
public class ClientRegisteredRepositoryHandler implements RegisteredClientRepository {

    /** 刷新令牌有效期默认 30 天 */
    private final static int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

    /** 请求令牌有效期默认 12 小时 */
    private final static int accessTokenValiditySeconds = 60 * 60 * 12;

    private final RemoteClientService remoteClientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * redis缓存支持
     *
     * @param clientId 客户端Id
     * @return 结果
     */
    @Override
    @SneakyThrows
    @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public RegisteredClient findByClientId(String clientId) {
        R<SysClientDto> R = remoteClientService.getInfoByClientIdInner(clientId);
        if (R.isFail())
            throw new OAuth2AuthorizationCodeRequestAuthenticationException(new OAuth2Error("客户端查询异常，请检查数据库链接"), null);
        SysClientDto clientDetails = R.getData();

        RegisteredClient.Builder builder = RegisteredClient.withId(clientDetails.getClientId())
                .clientId(clientDetails.getClientId())
                .clientSecret(SecurityConstants.NOOP + clientDetails.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);

        // 授权模式
        Optional.ofNullable(clientDetails.getAuthorizedGrantTypes())
                .ifPresent(grants -> StringUtils.commaDelimitedListToSet(grants)
                        .forEach(s -> builder.authorizationGrantType(new AuthorizationGrantType(s))));
        // 回调地址
        Optional.ofNullable(clientDetails.getWebServerRedirectUri()).ifPresent(redirectUri -> Arrays
                .stream(redirectUri.split(StrUtil.COMMA)).filter(StrUtil::isNotBlank).forEach(builder::redirectUri));

        // scope
        Optional.ofNullable(clientDetails.getScope()).ifPresent(
                scope -> Arrays.stream(scope.split(StrUtil.COMMA)).filter(StrUtil::isNotBlank).forEach(builder::scope));

        return builder
                .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(Optional
                                .ofNullable(clientDetails.getAccessTokenValidity()).orElse(accessTokenValiditySeconds)))
                        .refreshTokenTimeToLive(
                                Duration.ofSeconds(Optional.ofNullable(clientDetails.getRefreshTokenValidity())
                                        .orElse(refreshTokenValiditySeconds)))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(!BooleanUtil.toBoolean(clientDetails.getAutoApprove())).build())
                .build();
    }
}

