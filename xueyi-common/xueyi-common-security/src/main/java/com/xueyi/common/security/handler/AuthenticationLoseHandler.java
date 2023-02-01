package com.xueyi.common.security.handler;

import com.alibaba.fastjson2.JSON;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 身份认证失效处理
 *
 * @author xueyi
 */
@Slf4j
@Component
public class AuthenticationLoseHandler implements AuthenticationEntryPoint {

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
        log.error("[鉴权异常]请求路径:{},无法访问权限", request.getRequestURI());
        String msg = StrUtil.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        if (e instanceof InsufficientAuthenticationException) {
            msg = "请登陆后再访问";
        }
        ServletUtil.renderString(response, JSON.toJSONString(AjaxResult.error(HttpConstants.Status.UNAUTHORIZED.getCode(), msg)));
    }

}
