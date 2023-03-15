package com.xueyi.auth.support.base;

import com.xueyi.auth.login.base.IUserDetailsService;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义验证转换器
 *
 * @author xueyi
 */
public abstract class AuthenticationBaseConverter<T extends AuthenticationBaseToken> implements AuthenticationConverter {

    public final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * 判断授权类型
     *
     * @param grantType 授权类型
     * @return 结果
     */
    public abstract boolean support(String grantType);

    /**
     * 参数校验
     *
     * @param request 请求体
     */
    public void checkParams(HttpServletRequest request) {
        Map<String, IUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(IUserDetailsService.class);

        String grantType = request.getParameter(SecurityConstants.OAuth2ParameterNames.GRANT_TYPE.getCode());
        String accountType = request.getParameter(SecurityConstants.OAuth2ParameterNames.ACCOUNT_TYPE.getCode());

        Optional<IUserDetailsService> optional = userDetailsServiceMap.values().stream()
                .filter(service -> service.support(grantType, accountType))
                .max(Comparator.comparingInt(Ordered::getOrder));

        if (optional.isEmpty()) {
            throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
        }
        optional.get().checkParams(request);
    }

    /**
     * 构建token
     *
     * @param clientPrincipal      身份验证对象
     * @param requestedScopes      请求范围
     * @param additionalParameters 扩展信息
     * @return Token
     */
    public abstract T buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

    /**
     * 授权校验
     *
     * @param request 请求体
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 获取授权类型
        String grantType = request.getParameter(SecurityConstants.OAuth2ParameterNames.GRANT_TYPE.getCode());
        if (!support(grantType)) {
            return null;
        }

        MultiValueMap<String, String> parameters = ServletUtil.getParameters(request);
        // 获取请求范围
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StrUtil.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != NumberUtil.One) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "OAuth 2.0 Parameter: " + OAuth2ParameterNames.SCOPE, ACCESS_TOKEN_REQUEST_ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        Set<String> requestedScopes = null;
        if (StrUtil.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, StrUtil.SPACE)));
        }

        // 校验个性化参数
        checkParams(request);

        // 获取当前已经认证的客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "OAuth 2.0 Parameter: " + OAuth2ErrorCodes.INVALID_CLIENT, ACCESS_TOKEN_REQUEST_ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        // 扩展信息
        Map<String, Object> additionalParameters = parameters.entrySet().stream()
                .filter(e -> !e.getKey().equals(SecurityConstants.OAuth2ParameterNames.GRANT_TYPE.getCode()) && !e.getKey().equals(OAuth2ParameterNames.SCOPE))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        // 创建token
        return buildToken(clientPrincipal, requestedScopes, additionalParameters);
    }

}
