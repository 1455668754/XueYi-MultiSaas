package com.xueyi.common.security.handler;

import com.alibaba.fastjson2.JSON;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 身份认证失效处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class AuthenticationLoseHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     * 权限不足时的处理
     *
     * @param request  that resulted in an <code>AccessDeniedException</code>
     * @param response so that the user agent can be advised of the failure
     * @param e        that caused the invocation
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        int code = HttpConstants.Status.FORBIDDEN.getCode();
        String msg = null;
        if (e instanceof MissingCsrfTokenException) {
            msg = "缺少CSRF TOKEN,请从表单或HEADER传入";
        } else if (e instanceof InvalidCsrfTokenException) {
            msg = "无效的CSRF TOKEN";
        } else if (e instanceof CsrfException) {
            msg = e.getLocalizedMessage();
        } else if (e instanceof AuthorizationServiceException) {
            msg = AuthorizationServiceException.class.getSimpleName() + " " + e.getLocalizedMessage();
        }
        log.error("[鉴权异常]请求路径:{},禁止访问", request.getRequestURI());
        ServletUtil.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }

    /**
     * 认证失败处理
     *
     * @param request  that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param e        that caused the invocation
     * @throws IOException      异常
     * @throws ServletException 异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        int code = HttpConstants.Status.UNAUTHORIZED.getCode();
        log.error("[鉴权异常]请求路径:{},无法访问权限", request.getRequestURI());
        String msg = e.getClass().getSimpleName() + " " + e.getLocalizedMessage();
        if (e instanceof InsufficientAuthenticationException) {
            msg = "请登陆后再访问";
        }
        ServletUtil.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }

}
