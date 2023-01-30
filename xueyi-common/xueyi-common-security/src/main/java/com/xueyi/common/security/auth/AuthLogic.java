package com.xueyi.common.security.auth;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.exception.auth.NotLoginException;
import com.xueyi.common.core.utils.ServletUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Token 权限验证，逻辑实现类
 *
 * @author xueyi
 */
public class AuthLogic {

    public TokenService tokenService = SpringUtil.getBean(TokenService.class);

    /**
     * 会话注销
     */
    public void logout() {
        TenantConstants.AccountType accountType = BaseSecurityUtils.getAccountType();
        if (ObjectUtil.isNotNull(accountType)) {
            String token = getToken(accountType);
            if (StrUtil.isNotEmpty(token)) {
                verifyLoginUserExpire(token, accountType);
                logoutByToken(token, accountType);
            }
        }
    }

    /**
     * 检验用户是否已经登录，如未登录，则抛出异常
     */
    public void checkLogin() {
        getLoginUserRefresh();
    }

    /**
     * 获取用户信息并刷新有效期
     *
     * @return 用户缓存信息
     */
    public Object getLoginUserRefresh() {
        HttpServletRequest request = ServletUtil.getRequest();
        return getLoginUserRefresh(request);
    }

    /**
     * 获取用户信息并刷新有效期
     *
     * @return 用户缓存信息
     */
    public Object getLoginUserRefresh(HttpServletRequest request) {
        TenantConstants.AccountType accountType = TenantConstants.AccountType.getByCodeElseNull(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode()));
        if (ObjectUtil.isNull(accountType))
            throw new NotLoginException("无效的token");
        String token = getToken(request, accountType);
        Object loginUser = getLoginUser(token, accountType);
        verifyLoginUserExpire(token, accountType);
        return loginUser;
    }

    /**
     * 获取当前用户缓存信息, 如果未登录，则抛出异常
     *
     * @return 用户缓存信息
     */
    public Object getLoginUser(String token, TenantConstants.AccountType accountType) {
        if (StrUtil.isEmpty(token))
            throw new NotLoginException("未提供token");
        Object loginUser = switch (accountType) {
            case ADMIN, MEMBER -> SecurityUtils.getLoginUser();
        };
        if (ObjectUtil.isNull(loginUser))
            throw new NotLoginException("无效的token");
        return loginUser;
    }

    /**
     * 验证当前用户有效期, 如果相差不足360分钟，自动刷新缓存
     *
     * @param token       token
     * @param accountType 用户类型
     */
    public void verifyLoginUserExpire(String token, TenantConstants.AccountType accountType) {
        switch (accountType) {
            case ADMIN, MEMBER -> tokenService.verifyToken(token);
        }
    }

    /**
     * 获取用户Token
     *
     * @param accountType 用户类型
     * @return Token
     */
    public String getToken(TenantConstants.AccountType accountType) {
        return getToken(ServletUtil.getRequest(), accountType);
    }

    /**
     * 获取用户Token
     *
     * @param request     请求体
     * @param accountType 用户类型
     * @return Token
     */
    public String getToken(HttpServletRequest request, TenantConstants.AccountType accountType) {
        return switch (accountType) {
            case ADMIN, MEMBER -> SecurityUtils.getToken(request);
        };
    }

    /**
     * 会话注销，根据指定Token
     */
    public void logoutByToken(String token, TenantConstants.AccountType accountType) {
        switch (accountType) {
            case ADMIN, MEMBER -> tokenService.delLogin(token);
        }
    }
}
