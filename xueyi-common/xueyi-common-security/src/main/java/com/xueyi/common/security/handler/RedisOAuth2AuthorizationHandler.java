package com.xueyi.common.security.handler;

import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.base.BaseLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.Principal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis授权
 *
 * @author xueyi
 */
@Component
public class RedisOAuth2AuthorizationHandler implements OAuth2AuthorizationService {

    private static final String AUTHORIZATION = "token";

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        UsernamePasswordAuthenticationToken authenticationToken = authorization.getAttribute(Principal.class.getName());
        BaseLoginUser<?> loginUser = (BaseLoginUser<?>) authenticationToken.getPrincipal();
        Assert.notNull(loginUser, "authorization principal cannot be null");

        if (isRefreshToken(authorization)) {
            Assert.notNull(authorization.getRefreshToken(), "refreshToken cannot be null");
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            loginUser.setRefreshToken(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, loginUser.getEnterpriseId(), refreshToken.getTokenValue()));
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            loginUser.setAccessToken(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, loginUser.getEnterpriseId(), accessToken.getTokenValue()));
        }

        if (isState(authorization)) {
            String token = authorization.getAttribute(OAuth2ParameterNames.STATE);
            redisService.setCacheObject(buildKey(OAuth2ParameterNames.STATE, loginUser.getEnterpriseId(), token), authorization, (long) NumberUtil.Ten, TimeUnit.MINUTES);
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            Assert.notNull(authorizationCode, "authorizationCode cannot be null");
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            Assert.notNull(authorizationCodeToken.getIssuedAt(), "authorizationCodeToken issuedAt cannot be null");
            long between = ChronoUnit.MINUTES.between(authorizationCodeToken.getIssuedAt(), authorizationCodeToken.getExpiresAt());
            redisService.setCacheObject(buildKey(OAuth2ParameterNames.CODE, loginUser.getEnterpriseId(), authorizationCodeToken.getTokenValue()), authorization, between, TimeUnit.MINUTES);
        }

        tokenService.createCache(authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        BaseLoginUser<?> loginUser = authorization.getAttribute(Principal.class.getName());
        Assert.notNull(loginUser, "authorization principal cannot be null");
        List<String> keys = new ArrayList<>();
        if (isState(authorization)) {
            String token = authorization.getAttribute(OAuth2ParameterNames.STATE);
            keys.add(buildKey(OAuth2ParameterNames.STATE, loginUser.getEnterpriseId(), token));
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            keys.add(buildKey(OAuth2ParameterNames.CODE, loginUser.getEnterpriseId(), authorizationCodeToken.getTokenValue()));
        }

        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, loginUser.getEnterpriseId(), refreshToken.getTokenValue()));
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, loginUser.getEnterpriseId(), accessToken.getTokenValue()));
        }
        redisService.deleteObject(keys);
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Long enterpriseId = SecurityUtils.getEnterpriseId();
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");
        Assert.notNull(enterpriseId, "enterpriseId cannot be empty");
        return redisService.getCacheObject(buildKey(tokenType.getValue(), enterpriseId, token));
    }

    /**
     * 创建令牌
     *
     * @param type         类型
     * @param enterpriseId 企业Id
     * @param tokenValue   token
     * @return 令牌
     */
    private String buildKey(String type, Long enterpriseId, String tokenValue) {
        return StrUtil.format("{}:{}:{}:{}", AUTHORIZATION, enterpriseId.toString(), type, tokenValue);
    }

    private static boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean isCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        return ObjectUtil.isNotNull(authorizationCode);
    }

    private static boolean isRefreshToken(OAuth2Authorization authorization) {
        return ObjectUtil.isNotNull(authorization.getRefreshToken());
    }

    private static boolean isAccessToken(OAuth2Authorization authorization) {
        return ObjectUtil.isNotNull(authorization.getAccessToken());
    }

}
