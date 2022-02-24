package com.xueyi.system.authority.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.authority.service.ISysLoginService;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 登录管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysLoginServiceImpl implements ISysLoginService {

    @Autowired
    ISysEnterpriseService enterpriseService;

    @Autowired
    ISysUserService userService;

    @Autowired
    ISysMenuService menuService;

    /**
     * 登录校验 | 根据企业账号查询企业信息
     *
     * @param enterpriseName 企业账号
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto loginByEnterpriseName(String enterpriseName) {
        return enterpriseService.selectByName(enterpriseName);
    }

    /**
     * 登录校验 | 根据用户账号查询用户信息
     *
     * @param userName 用户账号
     * @param password 密码
     * @return 用户对象
     */
    @Override
    public SysUserDto loginByUser(String userName, String password) {
        return userService.userLogin(userName, password);
    }

    /**
     * 登录校验 | 获取角色数据权限
     *
     * @param roleList 角色信息集合
     * @param userType 用户标识
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(List<SysRoleDto> roleList, String userType) {
        Set<String> roles = new HashSet<>();
        // 租管租户拥有租管标识权限
        if (SecurityUtils.isAdminTenant())
            roles.add("administrator");
        // 超管用户拥有超管标识权限
        if (SysUserDto.isAdmin(userType))
            roles.add("admin");
        else
            roles.addAll(roleList.stream().map(SysRoleDto::getRoleKey).filter(StrUtil::isNotBlank).collect(Collectors.toSet()));
        return roles;
    }

    /**
     * 登录校验 | 获取菜单数据权限
     *
     * @param roleIds  角色Id集合
     * @param userType 用户标识
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(Set<Long> roleIds, String userType) {
        Set<String> perms = new HashSet<>();
        // 租管租户的超管用户拥有所有权限
        if (SecurityUtils.isAdminTenant() && SysUserDto.isAdmin(userType))
            perms.add("*:*:*");
        else {
            Set<String> set = SysUserDto.isAdmin(userType)
                    ? menuService.loginPermission()
                    : menuService.loginPermission(roleIds);
            // 常规租户的超管用户拥有本租户最高权限
            perms.addAll(set.stream().filter(StrUtil::isNotBlank).collect(Collectors.toSet()));
        }
        return perms;
    }

    /**
     * 登录校验 | 获取路由路径集合
     *
     * @param roleIds  角色Id集合
     * @param userType 用户标识
     * @return 路由路径集合
     */
    @Override
    public Map<String, String> getMenuRouteMap(Set<Long> roleIds, String userType) {
        if (SecurityUtils.isAdminTenant())
            return SysUserDto.isAdmin(userType)
                    ? menuService.getLessorRouteMap()
                    : menuService.getRouteMap(roleIds);
        else
            return SysUserDto.isAdmin(userType)
                    ? menuService.getRouteMap()
                    : menuService.getRouteMap(roleIds);
    }
}
