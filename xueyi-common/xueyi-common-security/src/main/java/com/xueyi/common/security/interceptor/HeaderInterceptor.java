package com.xueyi.common.security.interceptor;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Optional;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author xueyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {

    @Override
    @SneakyThrows
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            String accountType = ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());

            SecurityContextHolder.setEnterpriseId(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode()));
            SecurityContextHolder.setEnterpriseName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode()));
            SecurityContextHolder.setIsLessor(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.IS_LESSOR.getCode()));
            SecurityContextHolder.setUserId(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_ID.getCode()));
            SecurityContextHolder.setUserName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_NAME.getCode()));
            SecurityContextHolder.setNickName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.NICK_NAME.getCode()));
            SecurityContextHolder.setUserType(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_TYPE.getCode()));
            SecurityContextHolder.setAccessToken(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode()));
            SecurityContextHolder.setSourceName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode()));
            SecurityContextHolder.setUserKey(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_KEY.getCode()));
            SecurityContextHolder.setTenantIgnore(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.TENANT_IGNORE.getCode()));
            SecurityContextHolder.setAccountType(accountType);

            // 刷新用户有效期
            if (StrUtil.isNotBlank(accountType)) {
                Optional.of(SecurityUtils.getTokenService(accountType)).ifPresent(service -> service.refreshToken(request));
            }
        }
        return true;
    }

    @Override
    @SneakyThrows
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.remove();
    }
}