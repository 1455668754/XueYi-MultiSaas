package com.xueyi.auth.controller;

import com.xueyi.auth.form.RegisterBody;
import com.xueyi.auth.service.ISysLoginService;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.ApiAuth;
import com.xueyi.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * token 控制
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private ISysLoginService loginService;

    @PostMapping("/refresh")
    @ApiAuth(isAnonymous = true)
    public AjaxResult refresh(HttpServletRequest request) {
        Optional.ofNullable(SecurityUtils.getToken(request)).map(JwtUtil::getAccountType)
                .map(SecurityUtils::getTokenService).ifPresent(service -> service.refreshToken(request));
        return AjaxResult.success();
    }

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        loginService.register(registerBody);
        return AjaxResult.success();
    }
}