package com.xueyi.common.security.handler;

import com.xueyi.common.core.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

/**
 * 资源服务器 token处理器
 *
 * @author xueyi
 */
@Slf4j
@RequiredArgsConstructor
public class OpaqueTokenHandler implements OpaqueTokenIntrospector {

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2Authorization oldAuthorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (Objects.isNull(oldAuthorization)) {
            throw new InvalidBearerTokenException(token);
        }

        // 客户端模式默认返回
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(oldAuthorization.getAuthorizationGrantType())) {
            return new DefaultOAuth2AuthenticatedPrincipal(oldAuthorization.getPrincipalName(),
                    Objects.requireNonNull(oldAuthorization.getAccessToken().getClaims()), AuthorityUtils.NO_AUTHORITIES);
        }

        try {
            return Optional.of(oldAuthorization).map(item -> (UsernamePasswordAuthenticationToken) item.getAttributes().get(Principal.class.getName()))
                    .map(item -> (OAuth2AuthenticatedPrincipal) item.getPrincipal()).get();
        } catch (UsernameNotFoundException notFoundException) {
            log.warn("用户不存在 {}", notFoundException.getLocalizedMessage());
            throw notFoundException;
        } catch (Exception ex) {
            log.error("资源服务器 introspect Token error {}", ex.getLocalizedMessage());
        }
        throw new ServiceException("用户不存在");
    }
}
