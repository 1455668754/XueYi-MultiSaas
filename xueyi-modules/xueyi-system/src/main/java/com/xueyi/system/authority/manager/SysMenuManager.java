package com.xueyi.system.authority.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.SqlConstants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.web.entity.manager.TreeManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
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
     * 登录校验 | 获取租户全部菜单权限标识集合
     *
     * @param enterpriseId 企业Id
     * @return 菜单权限集合
     */
    public Set<String> loginPermission(Long enterpriseId) {
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
                    .eq(SysMenuDto::getEnterpriseId, TenantConstants.COMMON_TENANT_ID)
                    .or().eq(SysMenuDto::getEnterpriseId, enterpriseId);
        else
            menuQueryWrapper.eq(SysMenuDto::getEnterpriseId, enterpriseId);
        List<SysMenuDto> menuList = baseMapper.selectList(menuQueryWrapper);
        return CollUtil.isNotEmpty(menuList)
                ? menuList.stream().map(SysMenuDto::getPerms).collect(Collectors.toSet())
                : new HashSet<>();
    }

    /**
     * 登录校验 | 获取菜单权限标识集合
     *
     * @param roleIds      角色Id集合
     * @param enterpriseId 企业Id
     * @return 菜单权限标识集合
     */
    public Set<String> loginPermission(Set<Long> roleIds, Long enterpriseId) {
        // 1.获取用户可使用角色集内的所有菜单Ids
        List<SysRoleMenuMerge> roleModuleMenuMerges = roleMenuMergeMapper.selectList(
                Wrappers.<SysRoleMenuMerge>query().lambda()
                        .eq(SysRoleMenuMerge::getEnterpriseId, enterpriseId)
                        .in(SysRoleMenuMerge::getRoleId, roleIds));
        // 2.获取用户可使用的菜单
        if (CollUtil.isNotEmpty(roleModuleMenuMerges)) {
            List<Long> enterpriseIds = new ArrayList<Long>() {{
                this.add(TenantConstants.COMMON_TENANT_ID);
                this.add(enterpriseId);
            }};
            List<SysMenuDto> menuList = baseMapper.selectList(
                    Wrappers.<SysMenuDto>query().lambda()
                            .in(SysMenuDto::getId, roleModuleMenuMerges)
                            .in(SysMenuDto::getEnterpriseId, enterpriseIds));
            return CollUtil.isNotEmpty(menuList)
                    ? menuList.stream().map(SysMenuDto::getPerms).collect(Collectors.toSet())
                    : new HashSet<>();
        }
        return new HashSet<>();
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
        return baseMapper.selectList(
                Wrappers.<SysMenuDto>query().lambda()
                        .eq(SysMenuDto::getModuleId, moduleId)
                        .ne(SysMenuDto::getId, AuthorityConstants.MENU_TOP_NODE)
                        .eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DIR.getCode())
                        .or().eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                        .or().eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DETAILS.getCode()));
    }

    /**
     * 根据菜单类型及模块Id获取可配菜单集
     *
     * @param moduleId 模块Id
     * @param menuType 菜单类型
     * @return 菜单列表
     */
    public List<SysMenuDto> getMenuByMenuType(Long moduleId, String menuType){
        LambdaQueryWrapper<SysMenuDto> queryWrapper = new LambdaQueryWrapper<>();
        switch (Objects.requireNonNull(AuthorityConstants.MenuType.getValue(menuType))) {
            case BUTTON:
            case DETAILS:
                queryWrapper
                        .eq(SysMenuDto::getModuleId, moduleId)
                        .eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.MENU.getCode())
                        .or().eq(SysMenuDto::getMenuType, AuthorityConstants.MenuType.DIR.getCode())
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
        tenantMenuMergeMapper.delete(
                Wrappers.<SysTenantMenuMerge>update().lambda()
                        .in(SysTenantMenuMerge::getMenuId, children.stream().map(SysMenuDto::getId).collect(Collectors.toSet())));
        roleMenuMergeMapper.delete(
                Wrappers.<SysRoleMenuMerge>update().lambda()
                        .in(SysRoleMenuMerge::getMenuId, children.stream().map(SysMenuDto::getId).collect(Collectors.toSet())));
        return super.deleteChildren(id);
    }
}
