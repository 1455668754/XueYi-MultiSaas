package com.xueyi.common.security.auth;

import com.xueyi.common.core.constant.basic.TenantConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Token 权限验证工具类
 *
 * @author xueyi
 */
public class AuthUtil {

    /**
     * 底层的 AuthLogic 对象
     */
    public static AuthLogic authLogic = new AuthLogic();

    /**
     * 会话注销
     */
    public static void logout() {
        authLogic.logout();
    }

    /**
     * 会话注销，根据指定Token
     *
     * @param token 指定token
     */
    public static void logoutByToken(String token, TenantConstants.AccountType accountType) {
        authLogic.logoutByToken(token, accountType);
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin() {
        authLogic.checkLogin();
    }

    /**
     * 获取认证信息 | 不存在则创建
     *
     * @return 用户缓存信息
     */
    public static Authentication getAuthenticationDefaultNew() {
        return authLogic.getAuthenticationDefaultNew();
    }

    /**
     * 获取用户信息并刷新有效期
     *
     * @return 用户缓存信息
     */
    public static Object getLoginUserRefresh() {
        return authLogic.getLoginUserRefresh();
    }

    /**
     * 获取用户信息并刷新有效期
     *
     * @param request 请求体
     * @return 用户缓存信息
     */
    public static Object getLoginUserRefresh(HttpServletRequest request) {
        return authLogic.getLoginUserRefresh(request);
    }

    /**
     * 验证当前用户有效期
     */
    public static void verifyLoginUserExpire(String token, TenantConstants.AccountType accountType) {
        authLogic.verifyLoginUserExpire(token, accountType);
    }

}