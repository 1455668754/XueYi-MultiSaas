package com.xueyi.auth.support.password;

import com.xueyi.auth.support.base.AuthenticationBaseProvider;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义授权处理器 | 密码模式
 *
 * @author xueyi
 */
public class AuthenticationPasswordProvider extends AuthenticationBaseProvider<AuthenticationPasswordToken> {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationPasswordProvider.class);

    public AuthenticationPasswordProvider(AuthenticationManager authenticationManager, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String enterpriseName = (String) reqParameters.get(SecurityConstants.LoginParam.ENTERPRISE_NAME.getCode());
        String userName = (String) reqParameters.get(SecurityConstants.LoginParam.USER_NAME.getCode());
        String password = (String) reqParameters.get(SecurityConstants.LoginParam.PASSWORD.getCode());
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), enterpriseName);
        loginMap.put(SecurityConstants.BaseSecurity.USER_NAME.getCode(), userName);
        loginMap.put(SecurityConstants.BaseSecurity.PASSWORD.getCode(), password);
        return new UsernamePasswordAuthenticationToken(loginMap, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = AuthenticationPasswordToken.class.isAssignableFrom(authentication);
        LOGGER.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        assert registeredClient != null;
        if (!CollUtil.contains(registeredClient.getAuthorizationGrantTypes(), AuthorizationGrantType.PASSWORD))
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    }

}
