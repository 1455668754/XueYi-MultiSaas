package com.xueyi.system.authority.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.*;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.manager.TreeManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.mapper.merge.SysRoleMenuMergeMapper;
import com.xueyi.system.authority.mapper.merge.SysTenantMenuMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.xueyi.common.core.constant.SqlConstants.FIND_IN_SET;

/**
 * 菜单管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysMenuManager extends TreeManager<SysMenuDto, SysMenuMapper> {

    @Autowired
    SysTenantMenuMergeMapper tenantMenuMergeMapper;

    @Autowired
    SysRoleMenuMergeMapper roleMenuMergeMapper;

    /**
     * 登录校验 | 获取超管租户超管用户菜单集合
     *
     * @param enterpriseId 企业Id
     * @return 菜单集合
     */
    public List<SysMenuDto> loginLessorMenuList(Long enterpriseId) {
        // 1.获取超管租户超管用户可使用的菜单
        return baseMapper.selectList(
                Wrappers.<SysMenuDto>query().lambda()
                        .eq(SysMenuDto::getEnterpriseId, TenantConstants.COMMON_TENANT_ID)
                        .or().eq(SysMenuDto::getEnterpriseId, enterpriseId));
    }

    /**
     * 登录校验 | 获取租户全部菜单集合
     *
     * @param enterpriseId 企业Id
     * @return 菜单集合
     */
    public List<SysMenuDto> loginMenuList(Long enterpriseId) {
        // 1.获取租户授权的公共菜单Ids
        List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(
                Wrappers.<SysTenantMenuMerge>query().lambda()
                        .eq(SysTenantMenuMerge::getEnterpriseId, enterpriseId)
        );
        // 2.获取租户全部可使用的菜单
        LambdaQueryWrapper<SysMenuDto> menuQueryWrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(tenantMenuMerges))
            menuQueryWrapper
                    .in(SysMenuDto::getId, tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toSet()))
                    .and(i -> i
                            .eq(SysMenuDto::getEnterpriseId, TenantConstants.COMMON_TENANT_ID)
                            .or().eq(SysMenuDto::getEnterpriseId, enterpriseId));
        else
            menuQueryWrapper.eq(SysMenuDto::getEnterpriseId, enterpriseId);
        return baseMapper.selectList(menuQueryWrapper);
    }

    /**
     * 登录校验 | 获取菜单集合
     *
     * @param roleIds      角色Id集合
     * @param enterpriseId 企业Id
     * @return 菜单集合
     */
    public List<SysMenuDto> loginMenuList(Set<Long> roleIds, Long enterpriseId) {
        // 1.获取用户可使用角色集内的所有菜单Ids
        List<SysRoleMenuMerge> roleModuleMenuMerges = roleMenuMergeMapper.selectList(
                Wrappers.<SysRoleMenuMerge>query().lambda()
                        .eq(SysRoleMenuMerge::getEnterpriseId, enterpriseId)
                        .in(SysRoleMenuMerge::getRoleId, roleIds));
        // 2.获取用户可使用的菜单
        return CollUtil.isNotEmpty(roleModuleMenuMerges)
                ? baseMapper.selectList(
                Wrappers.<SysMenuDto>query().lambda()
                        .in(SysMenuDto::getId, roleModuleMenuMerges)
                        .and(i -> i
                                .eq(SysMenuDto::getEnterpriseId, enterpriseId)
                                .or().eq(SysMenuDto::getEnterpriseId, TenantConstants.COMMON_TENANT_ID)))
                : new ArrayList<>();
    }

    /**
     * 获取指定范围的公共菜单
     *
     * @return 菜单对象集合
     */
    public List<SysMenuDto> selectCommonList(Collection<? extends Serializable> idList) {
        return baseMapper.selectList(Wrappers.<SysMenuDto>query().lambda()
                .ne(SysMenuDto::getId, AuthorityConstants.MENU_TOP_NODE)
                .eq(SysMenuDto::getIsCommon, DictConstants.DicCommonPrivate.COMMON.getCode())
                .eq(SysMenuDto::getStatus, BaseConstants.Status.NORMAL.getCode())
                .func(i -> {
                    if (CollUtil.isNotEmpty(idList))
                        i.in(SysMenuDto::getId, idList);
                }));
    }


    /**
     * 校验菜单是否存在租户
     *
     * @param id 菜单Id
     * @return 菜单对象
     */
    public SysTenantMenuMerge checkMenuExistTenant(Long id) {
        return tenantMenuMergeMapper.selectOne(
                Wrappers.<SysTenantMenuMerge>query().lambda()
                        .eq(SysTenantMenuMerge::getMenuId, id)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验菜单是否存在角色
     *
     * @param id 菜单Id
     * @return 菜单对象
     */
    public SysRoleMenuMerge checkMenuExistRole(Long id) {
        return roleMenuMergeMapper.selectOne(
                Wrappers.<SysRoleMenuMerge>query().lambda()
                        .eq(SysRoleMenuMerge::getMenuId, id)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 根据模块Id查询菜单路由 | 不查默认菜单
     *
     * @param moduleId 模块Id
     * @return 菜单列表
     */
    public List<SysMenuDto> getRoutes(Long moduleId) {
        LambdaQueryWrapper<SysMenuDto> menuQueryWrapper = new LambdaQueryWrapper<>();
        if (SecurityUtils.isAdminUser()) {
            if (SecurityUtils.isNotAdminTenant()) {
                // 1.获取租户授权的公共菜单Ids
                List<SysTenantMenuMerge> tenantMenuMerges = tenantMenuMergeMapper.selectList(new LambdaQueryWrapper<>());
                // 2.获取租户全部可使用的菜单
                if (CollUtil.isNotEmpty(tenantMenuMerges)) {
                    menuQueryWrapper
                            .in(SysMenuDto::getId, tenantMenuMerges.stream().map(SysTenantMenuMerge::getMenuId).collect(Collectors.toSet()));
                } else {
                    menuQueryWrapper
                            .ne(SysMenuDto::getEnterpriseId, TenantConstants.COMMON_TENANT_ID);
                }
            }
        } else {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // 1.获取用户可使用角色集内的所有菜单Ids
            List<SysRoleMenuMerge> roleModuleMenuMerges = roleMenuMergeMapper.selectList(
                    Wrappers.<SysRoleMenuMerge>query().lambda()
                            .in(SysRoleMenuMerge::getRoleId, loginUser.getRoleIds()));
            // 2.获取用户可使用的菜单
            if (CollUtil.isNotEmpty(roleModuleMenuMerges)) {
                menuQueryWrapper
                        .in(SysMenuDto::getId, roleModuleMenuMerges);
            } else {
                return new ArrayList<>();
            }
        }
        menuQueryWrapper
                .eq(SysMenuDto::getModuleId, moduleId)
                .ne(SysMenuDto::getId, AuthorityConstants.MENU_TOP_NODE)
                .and(i -> i
                        .eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DIR.getCode())
                        .or().eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                        .or().eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DETAILS.getCode()));
        return baseMapper.selectList(menuQueryWrapper);
    }

    /**
     * 根据菜单类型及模块Id获取可配菜单集
     *
     * @param moduleId 模块Id
     * @param menuType 菜单类型
     * @return 菜单列表
     */
    public List<SysMenuDto> getMenuByMenuType(Long moduleId, String menuType) {
        LambdaQueryWrapper<SysMenuDto> queryWrapper = new LambdaQueryWrapper<>();
        switch (Objects.requireNonNull(AuthorityConstants.MenuType.getValue(menuType))) {
            case BUTTON:
            case DETAILS:
                queryWrapper
                        .eq(SysMenuDto::getModuleId, moduleId)
                        .and(i -> i
                                .eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                                .or().eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DIR.getCode()))
                        .or().eq(SysMenuDto::getId, AuthorityConstants.MENU_TOP_NODE);
                break;
            case MENU:
            case DIR:
                queryWrapper
                        .eq(SysMenuDto::getModuleId, moduleId)
                        .eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DIR.getCode())
                        .or().eq(SysMenuDto::getId, AuthorityConstants.MENU_TOP_NODE);
                break;
            default:
                return new ArrayList<>();
        }
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 新增菜单对象
     *
     * @param menu 菜单对象
     * @return 结果
     */
    @Override
    public int insert(SysMenuDto menu) {
        menu.setName(IdUtil.simpleUUID());
        return baseMapper.insert(menu);
    }

    /**
     * 新增菜单对象（批量）
     *
     * @param menuList 菜单对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insertBatch(Collection<SysMenuDto> menuList) {
        if (CollUtil.isNotEmpty(menuList))
            menuList.forEach(menu -> menu.setName(IdUtil.simpleUUID()));
        return baseMapper.insertBatch(menuList);
    }

    /**
     * 根据Id删除菜单对象 | 同步删除关联表数据
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        tenantMenuMergeMapper.delete(
                Wrappers.<SysTenantMenuMerge>update().lambda()
                        .eq(SysTenantMenuMerge::getMenuId, id));
        roleMenuMergeMapper.delete(
                Wrappers.<SysRoleMenuMerge>update().lambda()
                        .eq(SysRoleMenuMerge::getMenuId, id));
        return super.deleteById(id);
    }

    /**
     * 根据Id集合批量删除菜单对象 | 同步删除关联表数据
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        tenantMenuMergeMapper.delete(
                Wrappers.<SysTenantMenuMerge>update().lambda()
                        .in(SysTenantMenuMerge::getMenuId, idList));
        roleMenuMergeMapper.delete(
                Wrappers.<SysRoleMenuMerge>update().lambda()
                        .in(SysRoleMenuMerge::getMenuId, idList));
        return super.deleteByIds(idList);
    }

    /**
     * 根据Id删除其子菜单对象 | 同步删除关联表数据
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteChildren(Serializable id) {
        List<SysMenuDto> children = baseMapper.selectList(
                Wrappers.<SysMenuDto>update().lambda()
                        .apply(FIND_IN_SET, id, SqlConstants.Entity.ANCESTORS.getCode()));
        if (CollUtil.isNotEmpty(children)) {
            Set<Long> idSet = children.stream().map(SysMenuDto::getId).collect(Collectors.toSet());
            tenantMenuMergeMapper.delete(
                    Wrappers.<SysTenantMenuMerge>update().lambda()
                            .in(SysTenantMenuMerge::getMenuId, idSet));
            roleMenuMergeMapper.delete(
                    Wrappers.<SysRoleMenuMerge>update().lambda()
                            .in(SysRoleMenuMerge::getMenuId, idSet));
        }
        return super.deleteChildren(id);
    }
}
