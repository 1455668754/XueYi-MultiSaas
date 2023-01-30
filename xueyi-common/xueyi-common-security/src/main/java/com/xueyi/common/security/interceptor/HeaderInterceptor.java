package com.xueyi.common.security.interceptor;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author xueyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            SecurityContextHolder.setEnterpriseId(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode()));
            SecurityContextHolder.setEnterpriseName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode()));
            SecurityContextHolder.setIsLessor(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.IS_LESSOR.getCode()));
            SecurityContextHolder.setUserId(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_ID.getCode()));
            SecurityContextHolder.setUserName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_NAME.getCode()));
            SecurityContextHolder.setNickName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.NICK_NAME.getCode()));
            SecurityContextHolder.setUserType(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_TYPE.getCode()));
            SecurityContextHolder.setSourceName(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode()));
            SecurityContextHolder.setUserKey(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.USER_KEY.getCode()));
            SecurityContextHolder.setAccountType(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.remove();
    }
}