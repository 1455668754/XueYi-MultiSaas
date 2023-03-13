package com.xueyi.auth.service.impl;

import com.xueyi.auth.service.ISysLogService;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.service.ITokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 自定义登录操作类
 *
 * @author xueyi
 */
@Slf4j
public class AuthenticationEventHandlerImpl implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private final ISysLogService logService = SpringUtil.getBean(ISysLogService.class);

    /**
     * 登录成功
     */
    @Override
    @SneakyThrows
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
        BaseLoginUser loginUser = (BaseLoginUser) map.get(SecurityConstants.BaseSecurity.USER_INFO.getCode());
        Assert.notNull(loginUser, "loginUser cannot be null");
        // output login log
        logService.recordLoginInfo(loginUser, Constants.LOGIN_SUCCESS, "登录成功");
        ITokenService tokenService = SecurityUtils.getTokenService(loginUser.getAccountType().getCode());
        Assert.notNull(tokenService, "tokenService cannot be null");
        // 无状态 注意删除 context 上下文的信息
        SecurityContextHolder.clearContext();
        ServletUtil.webResponseWriter(response, AjaxResult.success(tokenService.createToken(loginUser)));
    }

    /**
     * 登录失败
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String enterpriseName = request.getParameter(SecurityConstants.LoginParam.ENTERPRISE_NAME.getCode());
        String userName = request.getParameter(SecurityConstants.LoginParam.USER_NAME.getCode());
        log.info("企业账号：{}，用户账号：{} 登录失败，异常：{}", enterpriseName, userName, exception.getLocalizedMessage());
        ServletUtil.webResponseWriter(response, AjaxResult.error(exception.getLocalizedMessage()));
    }
}
