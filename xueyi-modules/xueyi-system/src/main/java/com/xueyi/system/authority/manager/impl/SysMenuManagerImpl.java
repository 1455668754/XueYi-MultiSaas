package com.xueyi.system.authority.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.manager.impl.TreeManagerImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.authority.domain.merge.SysAuthGroupMenuMerge;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.model.SysMenuConverter;
import com.xueyi.system.authority.manager.ISysMenuManager;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.merge.SysAuthGroupMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
    private SysAuthGroupMenuMergeMapper authGroupMenuMergeMapper;

    @Autowired
    private SysRoleMenuMergeMapper roleMenuMergeMapper;

    /**
     * 获取全部状态正常公共菜单
     *
     * @return 菜单对象集合
     */
    @Override
    public List<SysMenuDto> selectCommonList() {
        List<SysMenuPo> menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                .eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode()));
        return mapperDto(menuList);
    }

    /**
     * 获取企业有权限且状态正常的菜单
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 菜单对象集合
     */
    @Override
    public List<SysMenuDto> selectEnterpriseList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType) {
        List<SysMenuPo> menuList;
        // 租管租户 && 超级管理员
        if (SysEnterpriseDto.isLessor(isLessor) && SysUserDto.isAdmin(userType)) {
            menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                    .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode()));
        } else {
            Set<Long> authGroupMenuIds;
            if (SysEnterpriseDto.isNotLessor(isLessor)) {
                if (CollUtil.isEmpty(authGroupIds)) {
                    return new ArrayList<>();
                }
                // 获取租户授权的公共菜单Ids
                List<SysAuthGroupMenuMerge> authGroupMenuMerges = authGroupMenuMergeMapper.selectList(Wrappers.<SysAuthGroupMenuMerge>lambdaQuery()
                        .in(SysAuthGroupMenuMerge::getAuthGroupId, authGroupIds));
                if (CollUtil.isEmpty(authGroupMenuMerges)) {
                    return new ArrayList<>();
                }
                authGroupMenuIds = authGroupMenuMerges.stream().map(SysAuthGroupMenuMerge::getMenuId).collect(Collectors.toSet());
            } else {
                authGroupMenuIds = new HashSet<>();
            }
            // 普通租户 && 超级管理员
            if (SysUserDto.isAdmin(userType)) {
                menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                        .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode())
                        .eq(CollUtil.isEmpty(authGroupMenuIds), SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                        .and(CollUtil.isNotEmpty(authGroupMenuIds), a -> a.eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                                .or().in(SysMenuPo::getId, authGroupMenuIds)));
            } else {
                if (CollUtil.isEmpty(roleIds)) {
                    return new ArrayList<>();
                }
                List<SysRoleMenuMerge> roleMenuMerges = roleMenuMergeMapper.selectList(Wrappers.<SysRoleMenuMerge>lambdaQuery()
                        .in(SysRoleMenuMerge::getRoleId, roleIds));
                if (CollUtil.isEmpty(roleMenuMerges)) {
                    return new ArrayList<>();
                }
                Set<Long> roleMenuIds = roleMenuMerges.stream().map(SysRoleMenuMerge::getMenuId).collect(Collectors.toSet());
                // 租管租户 && 普通用户
                if (SysEnterpriseDto.isLessor(isLessor)) {
                    menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                            .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode())
                            .in(SysMenuPo::getId, roleMenuIds));
                }
                // 普通租户 && 普通用户
                else {
                    menuList = baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                            .eq(SysMenuPo::getStatus, BaseConstants.Status.NORMAL.getCode())
                            .in(SysMenuPo::getId, roleMenuIds)
                            .eq(CollUtil.isEmpty(authGroupMenuIds), SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                            .and(CollUtil.isNotEmpty(authGroupMenuIds), a -> a.eq(SysMenuPo::getIsCommon, DictConstants.DicCommonPrivate.PRIVATE.getCode())
                                    .or().in(SysMenuPo::getId, authGroupMenuIds)));
                }
            }
        }
        return mapperDto(menuList);
    }

    /**
     * 根据模块Id查询菜单路由
     *
     * @param moduleId 模块Id
     * @param menuIds  菜单Ids
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getRoutes(Long moduleId, Collection<Long> menuIds) {
        if (ObjectUtil.isNull(moduleId) || CollUtil.isEmpty(menuIds)) {
            return new ArrayList<>();
        } else {
            return mapperDto(baseMapper.selectList(Wrappers.<SysMenuPo>lambdaQuery()
                    .in(SysMenuPo::getId, menuIds)
                    .eq(SysMenuPo::getModuleId, moduleId)
                    .and(i -> i
                            .eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.DIR.getCode())
                            .or().eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                            .or().eq(SysMenuPo::getMenuType, AuthorityConstants.MenuType.DETAILS.getCode()))));
        }
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
