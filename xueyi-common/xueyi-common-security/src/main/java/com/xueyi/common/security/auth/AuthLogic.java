package com.xueyi.common.security.auth;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.exception.auth.NotLoginException;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
     * 获取用户信息 | 不存在则创建
     *
     * @return 用户缓存信息
     */
    public Authentication getAuthenticationDefaultNew() {
        // 1.从认证信息中获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtil.isNotNull(authentication) && ObjectUtil.isNotNull(authentication.getPrincipal()))
            return authentication;
        HttpServletRequest request = ServletUtil.getRequest();
        TenantConstants.AccountType accountType = null;
        // 2.从缓存中获取
        if (ObjectUtil.isNotNull(request)) {
            accountType = TenantConstants.AccountType.getByCodeElseNull(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode()));
            if (ObjectUtil.isNotNull(accountType)) {
                Object loginUser = getLoginUser(accountType);
                if (ObjectUtil.isNotNull(loginUser)) {
                    return new UsernamePasswordAuthenticationToken(loginUser, null, null);
                }
            }
        }
        Object loginUser;
        // 3.重新构建
        if (ObjectUtil.isNotNull(accountType)) {
            loginUser = switch (accountType) {
                case ADMIN, MEMBER -> new LoginUser();
            };
        } else {
            loginUser = new LoginUser();
        }
        return new UsernamePasswordAuthenticationToken(loginUser, null, null);
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
     * 获取用户信息并刷新有效期, 如果未登录，则抛出异常
     *
     * @return 用户缓存信息
     */
    public Object getLoginUserRefresh(HttpServletRequest request) {
        TenantConstants.AccountType accountType = TenantConstants.AccountType.getByCodeElseNull(ServletUtil.getHeader(request, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode()));
        if (ObjectUtil.isNull(accountType))
            throw new NotLoginException("无效的token");
        String token = getToken(request, accountType);
        if (StrUtil.isEmpty(token))
            throw new NotLoginException("未提供token");
        Object loginUser = getLoginUser(accountType);
        if (ObjectUtil.isNull(loginUser))
            throw new NotLoginException("无效的token");
        verifyLoginUserExpire(token, accountType);
        return loginUser;
    }

    /**
     * 获取当前用户缓存信息
     *
     * @param accountType 用户类型
     * @return 用户缓存信息
     */
    public Object getLoginUser(TenantConstants.AccountType accountType) {
        return switch (accountType) {
            case ADMIN, MEMBER -> SecurityUtils.getLoginUser();
        };
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
