package com.xueyi.common.security.interceptor;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.security.auth.AuthUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author xueyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        SecurityContextHolder.setEnterpriseId(ServletUtils.getHeader(request, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode()));
        SecurityContextHolder.setEnterpriseName(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.ENTERPRISE_NAME.getCode()));
        SecurityContextHolder.setIsLessor(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.IS_LESSOR.getCode()));
        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.USER_ID.getCode()));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.USER_NAME.getCode()));
        SecurityContextHolder.setNickName(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.NICK_NAME.getCode()));
        SecurityContextHolder.setUserType(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.USER_TYPE.getCode()));
        SecurityContextHolder.setSourceName(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.SOURCE_NAME.getCode()));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants. BaseSecurity.USER_KEY.getCode()));
        SecurityContextHolder.setAccountType(ServletUtils.getHeader(request, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode()));

        String token = SecurityUtils.getToken();
        if (StrUtil.isNotEmpty(token)) {
            AuthUtil.verifyLoginUserExpire(token);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.remove();
    }
}