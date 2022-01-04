package com.xueyi.system.authority.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.SqlConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.domain.authority.dto.SysRoleDto;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import org.springframework.stereotype.Component;

/**
 * 角色管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysRoleManager extends BaseManager<SysRoleDto, SysRoleMapper> {

    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 角色对象
     */
    public SysRoleDto checkRoleCodeUnique(Long Id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysRoleDto>query().lambda()
                        .ne(SysRoleDto::getId, Id)
                        .eq(SysRoleDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 角色对象
     */
    public SysRoleDto checkRoleKeyUnique(Long Id, String roleKey) {
        return baseMapper.selectOne(
                Wrappers.<SysRoleDto>query().lambda()
                        .ne(SysRoleDto::getId, Id)
                        .eq(SysRoleDto::getRoleKey, roleKey)
                        .last(SqlConstants.LIMIT_ONE));
    }
}
