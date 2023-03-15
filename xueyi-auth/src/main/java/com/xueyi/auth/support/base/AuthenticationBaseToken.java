package com.xueyi.auth.support.base;

import com.xueyi.common.core.constant.basic.SecurityConstants.GrantType;
import com.xueyi.common.core.utils.core.StrUtil;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义授权模式
 *
 * @author xueyi
 */
public abstract class AuthenticationBaseToken extends AbstractAuthenticationToken {

    @Getter
    private final GrantType grantType;

    @Getter
    private final Authentication clientPrincipal;

    @Getter
    private final Set<String> scopes;

    @Getter
    private final Map<String, Object> additionalParameters;

    public AuthenticationBaseToken(GrantType grantType, Authentication clientPrincipal, @Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(grantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.grantType = grantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
        this.additionalParameters = Collections.unmodifiableMap(additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
    }

    /**
     * 扩展模式一般不需要密码
     */
    @Override
    public Object getCredentials() {
        return StrUtil.EMPTY;
    }

    /**
     * 获取用户名
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }

}
