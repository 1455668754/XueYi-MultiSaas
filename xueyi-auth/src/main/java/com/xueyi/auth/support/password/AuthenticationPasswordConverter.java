package com.xueyi.auth.support.password;

import com.xueyi.auth.support.base.AuthenticationBaseConverter;
import com.xueyi.common.core.constant.basic.SecurityConstants.GrantType;
import com.xueyi.common.core.utils.core.StrUtil;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Set;

/**
 * 自定义验证转换器 | 密码模式
 *
 * @author xueyi
 */
public class AuthenticationPasswordConverter extends AuthenticationBaseConverter<AuthenticationPasswordToken> {

    /**
     * 判断授权类型
     *
     * @param grantType 授权类型
     * @return 结果
     */
    @Override
    public boolean support(String grantType) {
        return StrUtil.equals(GrantType.PASSWORD.getCode(), grantType);
    }

    /**
     * 构建token
     *
     * @param clientPrincipal      身份验证对象
     * @param requestedScopes      请求范围
     * @param additionalParameters 扩展信息
     * @return Token
     */
    @Override
    public AuthenticationPasswordToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new AuthenticationPasswordToken(clientPrincipal, requestedScopes, additionalParameters);
    }
}
