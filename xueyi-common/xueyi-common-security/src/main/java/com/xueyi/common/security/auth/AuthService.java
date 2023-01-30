package com.xueyi.common.security.auth;

import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;

import java.util.Set;

import static com.xueyi.common.core.constant.basic.SecurityConstants.PERMISSION_ADMIN;

/**
 * 权限验证实现类
 *
 * @author xueyi
 */
public class AuthService {

    /**
     * 验证用户是否具备某权限
     *
     * @param authorities 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasAuthority(String... authorities) {
        if (ArrayUtil.isEmpty(authorities))
            return false;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtil.hasNull(loginUser, loginUser.getDataScope()) || CollUtil.isEmpty(loginUser.getDataScope().getPermissions()))
            return false;
        for (String authority : authorities)
            if (!hasAuthorities(loginUser.getDataScope().getPermissions(), authority))
                return false;
        return true;
    }

    /**
     * 验证用户是否不具备某权限(任一)
     *
     * @param authorities 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksAuthority(String... authorities) {
        return !hasAnyAuthority(authorities);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param authorities 权限字符串
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyAuthority(String... authorities) {
        if (ArrayUtil.isEmpty(authorities))
            return false;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtil.hasNull(loginUser, loginUser.getDataScope()) || CollUtil.isEmpty(loginUser.getDataScope().getPermissions()))
            return false;
        for (String authority : authorities)
            if (hasAuthorities(loginUser.getDataScope().getPermissions(), authority))
                return true;
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param roles 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String... roles) {
        if (ArrayUtil.isEmpty(roles))
            return false;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtil.hasNull(loginUser, loginUser.getDataScope()) || CollUtil.isEmpty(loginUser.getDataScope().getRoles()))
            return false;
        for (String role : roles)
            if (!hasRoles(loginUser.getDataScope().getRoles(), role))
                return false;
        return true;
    }

    /**
     * 验证用户是否不具备某角色(任一)
     *
     * @param roles 角色字符串
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String... roles) {
        return !hasAnyRole(roles);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 角色字符串
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRole(String... roles) {
        if (ArrayUtil.isEmpty(roles))
            return false;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtil.hasNull(loginUser, loginUser.getDataScope()) || CollUtil.isEmpty(loginUser.getDataScope().getRoles()))
            return false;
        for (String role : roles)
            if (hasRoles(loginUser.getDataScope().getRoles(), role))
                return true;
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasAuthorities(Set<String> permissions, String permission) {
        return CollUtil.contains(permissions, PERMISSION_ADMIN) || CollUtil.contains(permissions, StrUtil.trim(permission));
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色字符串
     * @return 用户是否具备某角色
     */
    private boolean hasRoles(Set<String> roles, String role) {
        return CollUtil.contains(roles, StrUtil.trim(role));
    }

}
