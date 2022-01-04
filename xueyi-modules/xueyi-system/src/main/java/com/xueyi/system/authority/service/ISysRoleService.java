package com.xueyi.system.authority.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.domain.authority.dto.SysRoleDto;

/**
 * 角色管理 服务层
 *
 * @author xueyi
 */
public interface ISysRoleService extends IBaseService<SysRoleDto> {

    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 结果 | true/false 唯一/不唯一
     */
    public boolean checkRoleCodeUnique(Long Id, String code);

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 结果 | true/false 唯一/不唯一
     */
    public boolean checkRoleKeyUnique(Long Id, String roleKey);
}
