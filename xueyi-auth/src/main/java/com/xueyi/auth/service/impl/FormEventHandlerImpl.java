package com.xueyi.auth.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.xueyi.auth.service.ISysLogService;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.service.ITokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * 表单登录操作类
 *
 * @author xueyi
 */
@Slf4j
@Component
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class FormEventHandlerImpl implements ApplicationListener<LogoutSuccessEvent>, LogoutSuccessHandler, AuthenticationFailureHandler {

    private final ISysLogService logService = SpringUtil.getBean(ISysLogService.class);

    /**
     * 退出登录 | 事件机制
     */
    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            log.info("用户：{} 退出成功", authentication.getPrincipal());
//        SysLog logVo = SysLogUtils.getSysLog();
//        logVo.setTitle("退出成功");
//        // 发送异步日志事件
//        Long startTime = System.currentTimeMillis();
//        Long endTime = System.currentTimeMillis();
//        logVo.setTime(endTime - startTime);
//
//        // 设置对应的token
//        WebUtils.getRequest().ifPresent(request -> logVo.setParams(request.getHeader(HttpHeaders.AUTHORIZATION)));
//
//        // 这边设置ServiceId
//        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
//            logVo.setServiceId(authentication.getCredentials().toString());
//        }
//        logVo.setCreateBy(authentication.getName());
//        logVo.setUpdateBy(authentication.getName());
//        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        }
    }

    /**
     * 退出登录
     */
    @Override
    @SneakyThrows
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = SecurityUtils.getToken(request);
        if (StrUtil.isNotEmpty(token)) {
            String accountType = JwtUtil.getAccountType(token);
            String userKey = JwtUtil.getUserKey(token);

            // 移除缓存
            if (StrUtil.isAllNotBlank(accountType, userKey)) {
                ITokenService tokenService = BaseSecurityUtils.getTokenService(accountType);
                BaseLoginUser loginUser = tokenService.getLoginUser();

                // 记录用户日志
                if (ObjectUtil.isNotNull(loginUser)) {
                    tokenService.removeTokenCache(loginUser);
                    logService.recordLoginInfo(loginUser, Constants.LOGOUT, "退出成功");
                } else {
                    String sourceName = JwtUtil.getSourceName(token);
                    Long enterpriseId = Long.valueOf(JwtUtil.getEnterpriseId(token));
                    String enterpriseName = JwtUtil.getEnterpriseName(token);
                    Long userId = Long.valueOf(JwtUtil.getUserId(token));
                    String userName = JwtUtil.getUserName(token);
                    String nickName = JwtUtil.getNickName(token);
                    logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, nickName, Constants.LOGOUT, "退出成功");
                }
            }
        }

//        // 回调跳转判断 | 暂时屏蔽
//        if (ObjectUtil.isNotNull(request)) {
//            // 获取请求参数中是否包含回调地址
//            String redirectUrl = request.getParameter(TokenConstants.REDIRECT_URL);
//            if (StrUtil.isNotBlank(redirectUrl)) {
//                response.sendRedirect(redirectUrl);
//            } else if (StrUtil.isNotBlank(request.getHeader(HttpHeaders.REFERER))) {
//                // 默认跳转referer地址
//                String referer = request.getHeader(HttpHeaders.REFERER);
//                response.sendRedirect(referer);
//            }
//        }

        ServletUtil.webResponseWriter(response, AjaxResult.success("退出成功"));
    }

    /**
     * 登录失败
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.debug("表单登录失败:{}", exception.getLocalizedMessage());
        String url = HttpUtil.encodeParams(String.format("/login?error=%s", exception.getMessage()),
                CharsetUtil.CHARSET_UTF_8);
        ServletUtil.getResponse().sendRedirect(url);
    }
}
