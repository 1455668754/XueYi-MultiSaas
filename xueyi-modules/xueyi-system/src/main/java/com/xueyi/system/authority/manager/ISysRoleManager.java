package com.xueyi.system.authority.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 权限模块 | 角色管理 数据封装层
 *
 * @author xueyi
 */
@Component
public interface ISysRoleManager extends IBaseManager<SysRoleQuery, SysRoleDto> {

    /**
     * 修改角色组织权限
     *
     * @param id        id
     * @param roleKey   权限字符串
     * @param dataScope 数据范围
     * @return 结果
     */
    int updateDataScope(Long id, String roleKey, String dataScope);

    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 角色对象
     */
    SysRoleDto checkRoleCodeUnique(Long Id, String code);

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 角色对象
     */
    SysRoleDto checkRoleKeyUnique(Long Id, String roleKey);
}
