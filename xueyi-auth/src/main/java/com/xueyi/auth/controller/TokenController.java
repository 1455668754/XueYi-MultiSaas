package com.xueyi.auth.controller;

import com.xueyi.auth.form.LoginBody;
import com.xueyi.auth.form.RegisterBody;
import com.xueyi.auth.service.ISysLoginService;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ISysLoginService sysLoginService;

    @PostMapping("login")
    public AjaxResult login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getEnterpriseName(), form.getUserName(), form.getPassword());
        // 获取登录token
        return AjaxResult.success(tokenService.createToken(userInfo));
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