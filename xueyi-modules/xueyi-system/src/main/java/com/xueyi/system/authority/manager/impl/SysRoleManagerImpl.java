package com.xueyi.system.authority.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.model.SysRoleConverter;
import com.xueyi.system.api.authority.domain.po.SysRolePo;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.manager.ISysRoleManager;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.domain.merge.SysRolePostMerge;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRolePostMergeMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.system.api.authority.domain.merge.MergeGroup.*;

/**
 * 角色管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysRoleManagerImpl extends BaseManagerImpl<SysRoleQuery, SysRoleDto, SysRolePo, SysRoleMapper, SysRoleConverter> implements ISysRoleManager {

    /**
     * 初始化从属关联关系
     *
     * @return 关系对象集合
     */
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>(){{
            add(new SlaveRelation(ROLE_SysRoleModuleMerge_GROUP, SysRoleModuleMergeMapper.class, SysRoleModuleMerge.class, OperateConstants.SubOperateLimit.ONLY_DEL));
            add(new SlaveRelation(ROLE_SysRoleMenuMerge_GROUP, SysRoleMenuMergeMapper.class, SysRoleMenuMerge.class, OperateConstants.SubOperateLimit.ONLY_DEL));
            add(new SlaveRelation(ROLE_SysRoleDeptMerge_GROUP, SysRoleDeptMergeMapper.class, SysRoleDeptMerge.class, OperateConstants.SubOperateLimit.ONLY_DEL));
            add(new SlaveRelation(ROLE_SysRolePostMerge_GROUP, SysRolePostMergeMapper.class, SysRolePostMerge.class, OperateConstants.SubOperateLimit.ONLY_DEL));
            add(new SlaveRelation(ROLE_SysOrganizeRoleMerge_GROUP, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge.class, OperateConstants.SubOperateLimit.ONLY_DEL));
        }};
    }

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
        return baseMapper.update(new SysRoleDto(),
                Wrappers.<SysRolePo>update().lambda()
                        .set(SysRolePo::getDataScope, dataScope)
                        .set(SysRolePo::getRoleKey, roleKey)
                        .eq(SysRolePo::getId, id));
    }

    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 角色对象
     */
    @Override
    public SysRoleDto checkRoleCodeUnique(Long Id, String code) {
        SysRolePo role = baseMapper.selectOne(
                Wrappers.<SysRolePo>query().lambda()
                        .ne(SysRolePo::getId, Id)
                        .eq(SysRolePo::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(role);
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 角色对象
     */
    @Override
    public SysRoleDto checkRoleKeyUnique(Long Id, String roleKey) {
        SysRolePo role = baseMapper.selectOne(
                Wrappers.<SysRolePo>query().lambda()
                        .ne(SysRolePo::getId, Id)
                        .eq(SysRolePo::getRoleKey, roleKey)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(role);
    }

}
