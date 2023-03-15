package com.xueyi.auth.handler;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义令牌
 *
 * @author xueyi
 */
public class CustomTokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        String clientId = context.getAuthorizationGrant().getName();
        claims.claim(SecurityConstants.BaseSecurity.CLIENT_ID.getCode(), clientId);
        // 客户端模式不返回具体用户信息
        if (SecurityConstants.GrantType.isClient(context.getAuthorizationGrantType().getValue()))
            return;

        Object loginUser = context.getPrincipal().getPrincipal();
        claims.claim(SecurityConstants.BaseSecurity.USER_INFO.getCode(), loginUser);
    }

}