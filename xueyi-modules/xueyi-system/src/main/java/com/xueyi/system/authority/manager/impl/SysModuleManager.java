package com.xueyi.system.authority.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.manager.impl.SubBaseManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.model.SysMenuConverter;
import com.xueyi.system.api.authority.domain.model.SysModuleConverter;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.authority.domain.merge.SysRoleModuleMerge;
import com.xueyi.system.authority.domain.merge.SysTenantModuleMerge;
import com.xueyi.system.authority.manager.ISysModuleManager;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.SysModuleMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleModuleMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantModuleMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模块管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysModuleManager extends SubBaseManager<SysModuleQuery, SysModuleDto, SysModulePo, SysModuleMapper, SysModuleConverter, SysMenuQuery, SysMenuDto, SysMenuPo, SysMenuMapper, SysMenuConverter> implements ISysModuleManager {

    @Autowired
    private SysTenantModuleMergeMapper tenantModuleMergeMapper;

    @Autowired
    private SysRoleModuleMergeMapper roleModuleMergeMapper;

    /**
     * 当前用户首页可展示的模块路由
     *
     * @param roleIds 角色Ids
     * @return 模块集合
     */
    @Override
    public List<SysModuleDto> getRoutes(Set<Long> roleIds) {
        // 超管用户 ? 租管租户 ? 查所有公共 + 所有私有模块 : 查权限内的公共 + 所有私有 : 根据拥有的角色查询权限
        if (SecurityUtils.isAdminUser()) {
            if (SecurityUtils.isAdminTenant()) {
                return BeanUtil.copyToList(baseMapper.selectList(
                        Wrappers.<SysModulePo>query().lambda()
                                .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())
                                .eq(SysModulePo::getHideModule, DictConstants.DicShowHide.SHOW.getCode())), SysModuleDto.class);
            } else {
                List<SysTenantModuleMerge> tenantModuleMerges = tenantModuleMergeMapper.selectList(Wrappers.query());
                return BeanUtil.copyToList(baseMapper.selectList(
                        Wrappers.<SysModulePo>query().lambda()
                                .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())
                                .eq(SysModulePo::getHideModule, DictConstants.DicShowHide.SHOW.getCode())
                                .and(i -> i.
                                        eq(SysModulePo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                                        .func(j -> {
                                            if (CollUtil.isNotEmpty(tenantModuleMerges)) {
                                                j.or(k -> k.
                                                        eq(SysModulePo::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                                                        .in(SysModulePo::getId, tenantModuleMerges.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList())));
                                            }
                                        })
                                )), SysModuleDto.class);
            }
        } else {
            if (CollUtil.isEmpty(roleIds))
                return new ArrayList<>();
            List<SysRoleModuleMerge> roleModuleMerges = roleModuleMergeMapper.selectList(
                    Wrappers.<SysRoleModuleMerge>query().lambda()
                            .in(SysRoleModuleMerge::getRoleId, roleIds));
            return CollUtil.isNotEmpty(roleModuleMerges)
                    ? BeanUtil.copyToList(baseMapper.selectList(
                    Wrappers.<SysModulePo>query().lambda()
                            .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())
                            .eq(SysModulePo::getHideModule, DictConstants.DicShowHide.SHOW.getCode())
                            .in(SysModulePo::getId, roleModuleMerges.stream().map(SysRoleModuleMerge::getModuleId).collect(Collectors.toList()))), SysModuleDto.class)
                    : new ArrayList<>();
        }
    }

    /**
     * 获取企业有权限的状态正常公共模块
     *
     * @return 模块对象集合
     */
    @Override
    public List<SysModuleDto> selectCommonList() {
        // 校验租管租户 ? 查询所有 : 查询租户-模块关联表,校验是否有数据 ? 查有关联权限的公共模块 : 返回空集合
        if (SecurityUtils.isAdminTenant()) {
            return BeanUtil.copyToList(baseMapper.selectList(Wrappers.<SysModulePo>query().lambda()
                    .eq(SysModulePo::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                    .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())), SysModuleDto.class);
        } else {
            List<SysTenantModuleMerge> tenantModuleMerges = tenantModuleMergeMapper.selectList(Wrappers.query());
            return CollUtil.isNotEmpty(tenantModuleMerges)
                    ? BeanUtil.copyToList(baseMapper.selectList(Wrappers.<SysModulePo>query().lambda()
                    .eq(SysModulePo::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                    .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())
                    .in(SysModulePo::getId, tenantModuleMerges.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList()))), SysModuleDto.class)
                    : new ArrayList<>();
        }
    }

    /**
     * 获取租户有权限且状态正常的模块
     *
     * @return 模块对象集合
     */
    @Override
    public List<SysModuleDto> selectTenantList() {
        // 校验租管租户 ? 查询所有 : 查询租户-模块关联表,校验是否有数据 ? 查有关联权限的公共模块与所有私有模块 : 查询所有私有模块
        if (SecurityUtils.isAdminTenant()) {
            return BeanUtil.copyToList(baseMapper.selectList(Wrappers.<SysModulePo>query().lambda()
                    .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())), SysModuleDto.class);
        } else {
            List<SysTenantModuleMerge> tenantModuleMerges = tenantModuleMergeMapper.selectList(Wrappers.query());
            return BeanUtil.copyToList(baseMapper.selectList(Wrappers.<SysModulePo>query().lambda()
                    .eq(SysModulePo::getStatus, BaseConstants.Status.NORMAL.getCode())
                    .func(i -> {
                        if (CollUtil.isNotEmpty(tenantModuleMerges))
                            i.and(j -> j.
                                    eq(SysModulePo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                                    .or().in(SysModulePo::getId, tenantModuleMerges.stream().map(SysTenantModuleMerge::getModuleId).collect(Collectors.toList()))
                            );
                        else
                            i.eq(SysModulePo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode());
                    })), SysModuleDto.class);
        }
    }

    /**
     * 根据Id删除模块对象 | 同步删除关联表数据
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        roleModuleMergeMapper.delete(
                Wrappers.<SysRoleModuleMerge>update().lambda()
                        .eq(SysRoleModuleMerge::getModuleId, id));
        return super.deleteById(id);
    }

    /**
     * 根据Id集合批量删除模块对象 | 同步删除关联表数据
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        roleModuleMergeMapper.delete(
                Wrappers.<SysRoleModuleMerge>update().lambda()
                        .in(SysRoleModuleMerge::getModuleId, idList));
        return super.deleteByIds(idList);
    }

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysMenuPo> queryWrapper, LambdaUpdateWrapper<SysMenuPo> updateWrapper, SysModuleDto module, Serializable key) {
        Serializable moduleId = ObjectUtil.isNotNull(module) ? module.getId() : key;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysMenuPo::getModuleId, moduleId);
        else
            updateWrapper.eq(SysMenuPo::getModuleId, moduleId);
    }
}
