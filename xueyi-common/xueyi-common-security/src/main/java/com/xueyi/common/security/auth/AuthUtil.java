package com.xueyi.common.security.auth;

import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.annotation.RequiresRoles;

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
    public static void logoutByToken(String token) {
        authLogic.logoutByToken(token);
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin() {
        authLogic.checkLogin();
    }

    /**
     * 验证当前用户有效期
     */
    public static void verifyLoginUserExpire(String token) {
        authLogic.verifyLoginUserExpire(token);
    }

    /**
     * 当前账号是否含有指定角色标识, 返回true或false
     *
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(String role) {
        return authLogic.hasRole(role);
    }

    /**
     * 当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
     *
     * @param role 角色标识
     */
    public static void checkRole(String role) {
        authLogic.checkRole(role);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotRoleException
     *
     * @param requiresRoles 角色权限注解
     */
    public static void checkRole(RequiresRoles requiresRoles) {
        authLogic.checkRole(requiresRoles);
    }

    /**
     * 当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     *
     * @param roles 角色标识数组
     */
    public static void checkRoleAnd(String... roles) {
        authLogic.checkRoleAnd(roles);
    }

    /**
     * 当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     *
     * @param roles 角色标识数组
     */
    public static void checkRoleOr(String... roles) {
        authLogic.checkRoleOr(roles);
    }

    /**
     * 当前账号是否含有指定权限, 返回true或false
     *
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPerm(String permission) {
        return authLogic.hasPerm(permission);
    }

    /**
     * 当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param permission 权限码
     */
    public static void checkPerm(String permission) {
        authLogic.checkPerm(permission);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param requiresPermissions 权限注解
     */
    public static void checkPerm(RequiresPermissions requiresPermissions) {
        authLogic.checkPerm(requiresPermissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，必须全部验证通过]
     *
     * @param permissions 权限码数组
     */
    public static void checkPermAnd(String... permissions) {
        authLogic.checkPermAnd(permissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     *
     * @param permissions 权限码数组
     */
    public static void checkPermOr(String... permissions) {
        authLogic.checkPermOr(permissions);
    }
}