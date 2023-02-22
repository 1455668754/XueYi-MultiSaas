package com.xueyi.auth.service.impl;

import cn.hutool.core.map.MapUtil;
import com.xueyi.auth.service.ISysLogService;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 自定义登录操作类
 *
 * @author xueyi
 */
@Slf4j
@Component
public class AuthenticationEventHandlerImpl implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysLogService logService;

    /**
     * 登录成功
     */
    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
        if (MapUtil.isNotEmpty(map)) {
            // TODO 登录日志
//            // 发送异步日志事件
//            PigUser userInfo = (PigUser) map.get(SecurityConstants.DETAILS_USER);
//            log.info("用户：{} 登录成功", userInfo.getName());
//            SecurityContextHolder.getContext().setAuthentication(accessTokenAuthentication);
//            SysLog logVo = SysLogUtils.getSysLog();
//            logVo.setTitle("登录成功");
//            String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
//            if (StrUtil.isNotBlank(startTimeStr)) {
//                Long startTime = Long.parseLong(startTimeStr);
//                Long endTime = System.currentTimeMillis();
//                logVo.setTime(endTime - startTime);
//            }
//            logVo.setCreateBy(userInfo.getName());
//            logVo.setUpdateBy(userInfo.getName());
//            SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        }

        // 输出token
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollUtil.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();

        // 无状态 注意删除 context 上下文的信息
        SecurityContextHolder.clearContext();
        ServletUtil.webResponseWriter(response, AjaxResult.success(accessTokenResponse));

//        tokenService.createToken(userInfo);
    }

    /**
     * 登录失败
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String username = request.getParameter(SecurityConstants.LoginParam.USER_NAME.getCode());

        log.info("用户：{} 登录失败，异常：{}", username, exception.getLocalizedMessage());
        // TODO 插入登录日志
//        SysLog logVo = SysLogUtils.getSysLog();
//        logVo.setTitle("登录失败");
//        logVo.setType(LogTypeEnum.ERROR.getType());
//        logVo.setException(exception.getLocalizedMessage());
//        // 发送异步日志事件
//        String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
//        if (StrUtil.isNotBlank(startTimeStr)) {
//            Long startTime = Long.parseLong(startTimeStr);
//            Long endTime = System.currentTimeMillis();
//            logVo.setTime(endTime - startTime);
//        }
//        logVo.setCreateBy(username);
//        logVo.setUpdateBy(username);
//        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        // 写出错误信息
//        sendErrorResponse(request, response, exception);
    }

//    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
//        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
//        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
//        String errorMessage;
//
//        if (exception instanceof OAuth2AuthenticationException authorizationException) {
//            errorMessage = StrUtil.isBlank(authorizationException.getError().getDescription())
//                    ? authorizationException.getError().getErrorCode()
//                    : authorizationException.getError().getDescription();
//        } else {
//            errorMessage = exception.getLocalizedMessage();
//        }
//
//        // 手机号登录
//        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
//        if (SecurityConstants.APP.equals(grantType)) {
//            errorMessage = MsgUtils.getSecurityMessage("AbstractUserDetailsAuthenticationProvider.smsBadCredentials");
//        }
//
//        this.errorHttpResponseConverter.write(R.failed(errorMessage), MediaType.APPLICATION_JSON, httpResponse);
//    }


}
