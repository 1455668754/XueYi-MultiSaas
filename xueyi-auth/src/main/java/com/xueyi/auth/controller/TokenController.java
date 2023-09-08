package com.xueyi.auth.controller;

import com.xueyi.auth.form.RegisterBody;
import com.xueyi.auth.service.ISysLoginService;
import com.xueyi.common.core.utils.JwtUtil;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.ApiAuth;
import com.xueyi.common.security.service.ITokenService;
import com.xueyi.common.security.service.TokenUserService;
import com.xueyi.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * token 控制
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private OAuth2AuthorizationService authorizationService;

    @Autowired
    private TokenUserService tokenService;

    @Autowired
    private ISysLoginService loginService;

    @PostMapping("/refresh")
    @ApiAuth(isAnonymous = true)
    public AjaxResult refresh(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        String accountType = JwtUtil.getAccountType(token);
        ITokenService tokenService = SecurityUtils.getTokenService(accountType);
        BaseLoginUser loginUser = tokenService.getLoginUser();
        OAuth2Authorization authorization = authorizationService.findByToken(SecurityUtils.getToken(), OAuth2TokenType.ACCESS_TOKEN);
        tokenService.refreshToken(request);
        return AjaxResult.success();
    }

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        loginService.register(registerBody);
        return AjaxResult.success();
    }
}