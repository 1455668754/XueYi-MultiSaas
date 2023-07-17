package com.xueyi.system.authority.manager;

import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.domain.merge.SysRoleMenuMerge;
import com.xueyi.system.authority.domain.merge.SysTenantMenuMerge;

import java.util.List;
import java.util.Set;

/**
 * 系统服务 | 权限模块 | 菜单管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysMenuManager extends ITreeManager<SysMenuQuery, SysMenuDto> {

    /**
     * 登录校验 | 获取超管租户超管用户菜单集合
     *
     * @return 菜单集合
     */
    List<SysMenuDto> loginLessorMenuList();

    /**
     * 登录校验 | 获取租户全部菜单集合
     *
     * @return 菜单集合
     */
    List<SysMenuDto> loginMenuList();

    /**
     * 登录校验 | 获取菜单集合
     *
     * @param roleIds 角色Id集合
     * @return 菜单集合
     */
    List<SysMenuDto> loginMenuList(Set<Long> roleIds);

    /**
     * 获取全部或指定范围内的状态正常公共菜单
     *
     * @return 菜单对象集合
     */
    List<SysMenuDto> selectCommonList();

    /**
     * 获取租户有权限且状态正常的菜单
     *
     * @return 菜单对象集合
     */
    List<SysMenuDto> selectTenantList();

    /**
     * 根据模块Id查询菜单路由 | 不查默认菜单
     *
     * @param moduleId 模块Id
     * @return 菜单列表
     */
    List<SysMenuDto> getRoutes(Long moduleId);

    /**
     * 校验菜单是否存在租户
     *
     * @param id 菜单Id
     * @return 菜单对象
     */
    SysTenantMenuMerge checkMenuExistTenant(Long id);

    /**
     * 校验菜单是否存在角色
     *
     * @param id 菜单Id
     * @return 菜单对象
     */
    SysRoleMenuMerge checkMenuExistRole(Long id);
}
