package com.xueyi.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.xueyi.common.core.constant.basic.Constants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.auth.AuthUtil;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 自定义退出成功操作类
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLogService logService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = SecurityUtils.getToken(request);
        if (StrUtil.isNotEmpty(token)) {
            LoginUser loginUser = tokenService.getLoginUser(request);
            String accountType = JwtUtil.getAccountType(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token, TenantConstants.AccountType.getByCode(accountType));
            if (ObjectUtil.isNotNull(loginUser)) {
                String sourceName = JwtUtil.getSourceName(token);
                Long enterpriseId = Long.valueOf(JwtUtil.getEnterpriseId(token));
                String enterpriseName = JwtUtil.getEnterpriseName(token);
                Long userId = Long.valueOf(JwtUtil.getUserId(token));
                String userName = JwtUtil.getUserName(token);
                String userNick = loginUser.getUser().getNickName();
                // 记录用户退出日志
                logService.recordLoginInfo(sourceName, enterpriseId, enterpriseName, userId, userName, userNick, Constants.LOGOUT, "退出成功");
            }
        }
        ServletUtil.renderString(response, JSON.toJSONString(AjaxResult.success("退出成功")));
    }
}

