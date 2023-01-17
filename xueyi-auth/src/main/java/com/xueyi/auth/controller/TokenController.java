package com.xueyi.auth.controller;

import com.xueyi.auth.form.LoginBody;
import com.xueyi.auth.form.RegisterBody;
import com.xueyi.auth.service.SysLoginService;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.auth.AuthUtil;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * token 控制
 *
 * @author xueyi
 */
@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getEnterpriseName(), form.getUserName(), form.getPassword());
        // 获取登录token
        return AjaxResult.success(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public AjaxResult logout(HttpServletRequest request) {
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
                sysLoginService.logout(sourceName, enterpriseId, enterpriseName, userId, userName, userNick);
            }
        }
        return AjaxResult.success();
    }

    @PostMapping("refresh")
    public AjaxResult refresh(HttpServletRequest request) {
        tokenService.refreshToken(request);
        return AjaxResult.success();
    }

    @PostMapping("register")
    public AjaxResult register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return AjaxResult.success();
    }
}