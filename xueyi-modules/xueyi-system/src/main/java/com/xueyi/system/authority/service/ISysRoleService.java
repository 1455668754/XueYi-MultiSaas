package com.xueyi.system.authority.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;

/**
 * 系统服务 | 权限模块 | 角色管理 服务层
 *
 * @author xueyi
 */
public interface ISysRoleService extends IBaseService<SysRoleQuery, SysRoleDto> {

    /**
     * 根据Id查询角色信息对象 | 含功能权限
     *
     * @param id Id
     * @return 角色信息对象
     */
    SysRoleDto selectAuthById(Long id);

    /**
     * 根据Id查询角色信息对象 | 含数据权限
     *
     * @param id Id
     * @return 角色信息对象
     */
    SysRoleDto selectDataById(Long id);

    /**
     * 修改角色功能权限
     *
     * @param role 角色对象
     * @return 结果
     */
    int updateRoleAuth(SysRoleDto role);

    /**
     * 修改角色组织权限
     *
     * @param role 角色对象
     * @return 结果
     */
    int updateDataScope(SysRoleDto role);

}
