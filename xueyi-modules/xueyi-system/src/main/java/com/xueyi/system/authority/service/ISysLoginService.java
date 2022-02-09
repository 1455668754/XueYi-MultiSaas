package com.xueyi.system.authority.service;

import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录管理 服务层
 *
 * @author xueyi
 */
public interface ISysLoginService {

    /**
     * 登录校验 | 根据企业账号查询企业信息
     *
     * @param enterpriseName 企业账号
     * @return 企业对象
     */
    SysEnterpriseDto loginByEnterpriseName(String enterpriseName);

    /**
     * 登录校验 | 根据用户账号查询用户信息
     *
     * @param userName     用户账号
     * @param password     密码
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 用户对象
     */
    SysUserDto loginByUser(String userName, String password, Long enterpriseId, String sourceName);

    /**
     * 登录校验 | 获取角色数据权限
     *
     * @param roleList 角色信息集合
     * @param isLessor 租户标识
     * @param userType 用户标识
     * @return 角色权限信息
     */
    Set<String> getRolePermission(List<SysRoleDto> roleList, String isLessor, String userType);

    /**
     * 登录校验 | 获取菜单数据权限
     *
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(Set<Long> roleIds, String isLessor, String userType, Long enterpriseId, String sourceName);

    /**
     * 登录校验 | 获取路由路径集合
     *
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 路由路径集合
     */
    Map<String, String> getMenuRouteMap(Set<Long> roleIds, String isLessor, String userType, Long enterpriseId, String sourceName);
}
