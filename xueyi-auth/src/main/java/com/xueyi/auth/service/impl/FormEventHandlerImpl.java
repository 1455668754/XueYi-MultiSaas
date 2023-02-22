package com.xueyi.auth.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.xueyi.auth.service.ISysLogService;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FormEventHandlerImpl implements ApplicationListener<LogoutSuccessEvent>, LogoutSuccessHandler, AuthenticationFailureHandler {

    private static final String REDIRECT_URL = "redirect_url";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysLogService logService;

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
        if (response == null) {
            return;
        }

        // 获取请求参数中是否包含 回调地址
        String redirectUrl = request.getParameter(REDIRECT_URL);
        if (cn.hutool.core.util.StrUtil.isNotBlank(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        } else if (cn.hutool.core.util.StrUtil.isNotBlank(request.getHeader(HttpHeaders.REFERER))) {
            // 默认跳转referer 地址
            String referer = request.getHeader(HttpHeaders.REFERER);
            response.sendRedirect(referer);
        }

//        String token = SecurityUtils.getToken(request);
//        if (StrUtil.isNotEmpty(token)) {
//            LoginUser loginUser = tokenService.getLoginUser(request);
//            String accountType = JwtUtil.getAccountType(token);
//            // 删除用户缓存记录
//            AuthUtil.logoutByToken(token, TenantConstants.AccountType.getByCode(accountType));
//            if (ObjectUtil.isNotNull(loginUser)) {
//                String sourceName = JwtUtil.getSourceName(token);
//                Long enterpriseId = Long.valueOf(JwtUtil.getEnterpriseId(token));
//                String enterpriseName = JwtUtil.getEnterpriseName(token);
//                Long userId = Long.valueOf(JwtUtil.getUserId(token));
//                String userName = JwtUtil.getUserName(token);
//                String userNick = loginUser.getUser().getNickName();
//                // 记录用户退出日志
//                logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGOUT, "退出成功");
//            }
//        }
//        ServletUtil.renderString(response, JSON.toJSONString(AjaxResult.success("退出成功")));
    }

    /**
     * 登录失败
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.debug("表单登录失败:{}", exception.getLocalizedMessage());
        String url = HttpUtil.encodeParams(String.format("/token/login?error=%s", exception.getMessage()),
                CharsetUtil.CHARSET_UTF_8);
        ServletUtil.getResponse().sendRedirect(url);
    }
}
