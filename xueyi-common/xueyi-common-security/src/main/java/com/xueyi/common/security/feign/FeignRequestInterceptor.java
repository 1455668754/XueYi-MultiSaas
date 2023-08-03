package com.xueyi.common.security.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.ip.IpUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * feign 请求拦截器
 *
 * @author xueyi
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ServletUtil.getRequest();
        if (ObjectUtil.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtil.getHeaders(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.IS_LESSOR.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.USER_ID.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.USER_NAME.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.NICK_NAME.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.USER_TYPE.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.USER_KEY.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.AUTHORIZATION_HEADER.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.SUPPLY_AUTHORIZATION_HEADER.getCode());
            setHeaderKey(requestTemplate, headers, SecurityConstants.BaseSecurity.TENANT_IGNORE.getCode());

            String accountType = SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode();

            // 会员端专属参数
            if (StrUtil.equals(SecurityConstants.AccountType.MEMBER.getCode(), accountType)) {
                setHeaderKey(requestTemplate, headers, SecurityConstants.MemberSecurity.APPLICATION_ID.getCode());
                setHeaderKey(requestTemplate, headers, SecurityConstants.MemberSecurity.APP_ID.getCode());
            }
            // 平台端专属参数
            else if (StrUtil.equals(SecurityConstants.AccountType.PLATFORM.getCode(), accountType)) {
                setHeaderKey(requestTemplate, headers, SecurityConstants.PlatformSecurity.APP_ID.getCode());
            }

            // 配置客户端IP
            requestTemplate.header("X-Forwarded-For", IpUtil.getIpAddr());
        }
    }

    /**
     * 更替请求头内容
     *
     * @param requestTemplate 请求模板对象
     * @param headers         请求头内容
     * @param headerKey       请求头键值
     */
    private void setHeaderKey(RequestTemplate requestTemplate, Map<String, String> headers, String headerKey) {
        String key = headers.get(headerKey);
        if (StrUtil.isNotBlank(key))
            requestTemplate.header(headerKey, key);
    }
}