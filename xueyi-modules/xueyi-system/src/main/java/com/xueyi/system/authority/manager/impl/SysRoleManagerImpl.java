package com.xueyi.system.authority.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.po.SysRolePo;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import com.xueyi.system.authority.domain.model.SysRoleConverter;
import com.xueyi.system.authority.manager.ISysRoleManager;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 权限模块 | 角色管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysRoleManagerImpl extends BaseManagerImpl<SysRoleQuery, SysRoleDto, SysRolePo, SysRoleMapper, SysRoleConverter> implements ISysRoleManager {

    /**
     * 修改角色组织权限
     *
     * @param id        id
     * @param roleKey   权限字符串
     * @param dataScope 数据范围
     * @return 结果
     */
    @Override
    public int updateDataScope(Long id, String roleKey, String dataScope) {
        return baseMapper.update(null,
                Wrappers.<SysRolePo>update().lambda()
                        .set(SysRolePo::getDataScope, dataScope)
                        .eq(SysRolePo::getId, id));
    }
}
