package com.xueyi.system.authority.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.manager.impl.TreeManagerImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;
import com.xueyi.system.authority.domain.model.SysMenuConverter;
import com.xueyi.system.authority.manager.ISysMenuManager;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantMenuMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 菜单管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysMenuManagerImpl extends TreeManagerImpl<SysMenuQuery, SysMenuDto, SysMenuPo, SysMenuMapper, SysMenuConverter> implements ISysMenuManager {

    @Autowired
    SysTenantMenuMergeMapper tenantMenuMergeMapper;

    @Autowired
    SysRoleMenuMergeMapper roleMenuMergeMapper;

    /**
     * 登录校验 | 获取超管租户超管用户菜单集合
     *
     * @return 菜单集合
     */
    @Override
    public List<SysMenuDto> loginLessorMenuList() {
        // 1.获取超管租户超管用户可使用的菜单
        return baseConverter.mapperDto(baseMapper.selectList(Wrappers.query()));
    }

    /**
     * 登录校验 | 获取租户全部菜单集合
     *
     * @return 菜单集合
     */
    @Override
    public List<SysMenuDto> loginMenuList() {
        // 1.获取租户授权的公共菜单Ids
        List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(Wrappers.query());
        List<SysMenuPo> menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                .eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                .func(i -> {
                    if (CollUtil.isNotEmpty(tenantMenuMerges)) {
                        i.or().in(SysMenuPo::getId, tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toSet()));
                    }
                }));
        // 2.获取租户全部可使用的菜单
        return baseConverter.mapperDto(menuList);
    }

    /**
     * 登录校验 | 获取菜单集合
     *
     * @param roleIds 角色Id集合
     * @return 菜单集合
     */
    @Override
    public List<SysMenuDto> loginMenuList(Set<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds))
            return new ArrayList<>();
        // 1.获取用户可使用角色集内的所有菜单Ids
        List<SysRoleMenuMerge> roleMenuMerges = roleMenuMergeMapper.selectList(
                Wrappers.<SysRoleMenuMerge>lambdaQuery()
                        .in(SysRoleMenuMerge::getRoleId, roleIds));
        // 2.获取用户可使用的菜单
        return CollUtil.isNotEmpty(roleMenuMerges)
                ? baseConverter.mapperDto(baseMapper.selectList(
                Wrappers.<SysMenuPo>lambdaQuery()
                        .in(SysMenuPo::getId, roleMenuMerges.stream().map(SysRoleMenuMerge::getMenuId).collect(Collectors.toSet()))))
                : new ArrayList<>();
    }

    /**
     * 获取全部或指定范围内的状态正常公共菜单
     *
     * @return 菜单对象集合
     */
    @Override
    public List<SysMenuDto> selectCommonList() {
        // 校验租管租户 ? 查询所有 : 查询租户-菜单关联表,校验是否有数据 ? 查有关联权限的公共菜单与所有私有菜单 : 查询所有私有菜单
        if (SecurityUserUtils.isAdminTenant()) {
            List<SysMenuPo> menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                    .eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                    .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode()));
            return baseConverter.mapperDto(menuList);
        } else {
            List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(Wrappers.query());
            return CollUtil.isNotEmpty(tenantMenuMerges)
                    ? baseConverter.mapperDto(baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                    .eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                    .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode())
                    .in(SysMenuPo::getId, tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList()))))
                    : new ArrayList<>();
        }
    }

    /**
     * 获取租户有权限且状态正常的菜单
     *
     * @return 菜单对象集合
     */
    @Override
    public List<SysMenuDto> selectTenantList() {
        // 校验租管租户 ? 查询所有 : 查询租户-菜单关联表,校验是否有数据 ? 查有关联权限的公共菜单 : 返回空集合
        if (SecurityUserUtils.isAdminTenant()) {
            List<SysMenuPo> menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                    .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode()));
            return baseConverter.mapperDto(menuList);
        } else {
            List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(Wrappers.query());
            List<SysMenuPo> menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                    .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode())
                    .func(i -> {
                        if (CollUtil.isNotEmpty(tenantMenuMerges))
                            i.and(j -> j.
                                    eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                                    .or().in(SysMenuPo::getId, tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toList()))
                            );
                        else
                            i.eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode());
                    }));
            return baseConverter.mapperDto(menuList);
        }
    }

    /**
     * 根据模块Id查询菜单路由 | 不查默认菜单
     *
     * @param moduleId 模块Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getRoutes(Long moduleId) {
        LambdaQueryWrapper<SysMenuPo> menuQueryWrapper = new LambdaQueryWrapper<>();
        if (SecurityUserUtils.isAdminUser()) {
            if (SecurityUserUtils.isNotAdminTenant()) {
                // 1.获取租户授权的公共菜单Ids
                List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(Wrappers.query());
                // 2.获取租户全部可使用的菜单
                menuQueryWrapper
                        .eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                        .func(i -> {
                            if (CollUtil.isNotEmpty(tenantMenuMerges)) {
                                i.or().in(SysMenuPo::getId, tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toSet()));
                            }
                        });
            }
        } else {
            DataScope dataScope = SecurityUserUtils.getDataScope();
            Set<Long> roleIds = dataScope.getRoleIds();
            if (CollUtil.isEmpty(roleIds))
                return new ArrayList<>();
            // 1.获取用户可使用角色集内的所有菜单Ids
            List<SysRoleMenuMerge> roleMenuMerges = roleMenuMergeMapper.selectList(
                    Wrappers.<SysRoleMenuMerge>lambdaQuery()
                            .in(SysRoleMenuMerge::getRoleId, roleIds));
            // 2.获取用户可使用的菜单
            if (CollUtil.isNotEmpty(roleMenuMerges)) {
                menuQueryWrapper
                        .in(SysMenuPo::getId, roleMenuMerges.stream().map(SysRoleMenuMerge::getMenuId).collect(Collectors.toSet()));
            } else {
                return new ArrayList<>();
            }
        }
        menuQueryWrapper
                .eq(SysMenuPo::getModuleId, moduleId)
                .and(i -> i
                        .eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.DIR.getCode())
                        .or().eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                        .or().eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.DETAILS.getCode()));
        return baseConverter.mapperDto(baseMapper.selectList(menuQueryWrapper));
    }

    /**
     * 校验菜单是否存在租户
     *
     * @param id 菜单Id
     * @return 菜单对象
     */
    @Override
    public SysTenantMenuMerge checkMenuExistTenant(Long id) {
        return tenantMenuMergeMapper.selectOne(
                Wrappers.<SysTenantMenuMerge>lambdaQuery()
                        .eq(SysTenantMenuMerge::getMenuId, id)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验菜单是否存在角色
     *
     * @param id 菜单Id
     * @return 菜单对象
     */
    @Override
    public SysRoleMenuMerge checkMenuExistRole(Long id) {
        return roleMenuMergeMapper.selectOne(
                Wrappers.<SysRoleMenuMerge>lambdaQuery()
                        .eq(SysRoleMenuMerge::getMenuId, id)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 查询条件构造 | 列表查询
     *
     * @param query 数据查询对象
     * @return 条件构造器
     */
    protected LambdaQueryWrapper<SysMenuPo> selectListQuery(SysMenuQuery query) {
        return Wrappers.<SysMenuPo>lambdaQuery(query)
                .func(i -> {
                    if (StrUtil.isNotBlank(query.getMenuTypeLimit())) {
                        switch (AuthorityConstants.MenuType.getByCode(query.getMenuTypeLimit())) {
                            case BUTTON, DETAILS -> i
                                    .and(ai -> ai.eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                                            .or().eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.DIR.getCode()));
                            case MENU, DIR -> i
                                    .eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.DIR.getCode());
                        }
                    }
                });
    }

    /**
     * 修改条件构造 | 树子数据修改
     *
     * @param menu          数据传输对象
     * @param updateWrapper 更新条件构造器
     * @return 条件构造器
     */
    @Override
    protected LambdaUpdateWrapper<SysMenuPo> updateChildrenWrapper(SysMenuDto menu, LambdaUpdateWrapper<SysMenuPo> updateWrapper) {
        updateWrapper.set(SysMenuPo::getModuleId, menu.getModuleId());
        return updateWrapper;
    }
}
