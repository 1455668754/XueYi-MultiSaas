package com.xueyi.system.authority.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.TreeServiceImpl;
import com.xueyi.system.api.domain.authority.dto.SysMenuDto;
import com.xueyi.system.authority.manager.SysMenuManager;
import com.xueyi.system.authority.mapper.SysMenuMapper;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.utils.route.RouteUtils;
import com.xueyi.system.utils.route.RouterVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class SysMenuServiceImpl extends TreeServiceImpl<SysMenuDto, SysMenuManager, SysMenuMapper> implements ISysMenuService {

    /**
     * 登录校验 | 获取租户全部菜单权限标识集合
     *
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 菜单权限集合
     */
    @Override
    @DS("#sourceName")
    public Set<String> loginPermission(Long enterpriseId, String sourceName) {
        return baseManager.loginPermission(enterpriseId);
    }

    /**
     * 登录校验 | 获取菜单权限标识集合
     *
     * @param roleIds      角色Id集合
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 菜单权限集合
     */
    @Override
    @DS("#sourceName")
    public Set<String> loginPermission(Set<Long> roleIds, Long enterpriseId, String sourceName) {
        return baseManager.loginPermission(roleIds, enterpriseId);
    }

    /**
     * 根据模块Id查询菜单路由
     *
     * @param moduleId 模块Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getRoutes(Long moduleId) {
        return baseManager.getRoutes(moduleId);
    }

    /**
     * 根据菜单类型及模块Id获取可配菜单集
     *
     * @param moduleId 模块Id
     * @param menuType 菜单类型
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getMenuByMenuType(Long moduleId, String menuType){
        return baseManager.getMenuByMenuType(moduleId, menuType);
    }

    /**
     * 校验菜单是否存在租户
     *
     * @param id 菜单Id
     * @return 结果 | true/false 有/无
     */
    public boolean checkMenuExistTenant(Long id){
        return ObjectUtil.isNotNull(baseManager.checkMenuExistTenant(id));
    }

    /**
     * 校验菜单是否存在角色
     *
     * @param id 菜单Id
     * @return 结果 | true/false 有/无
     */
    public boolean checkMenuExistRole(Long id){
        return ObjectUtil.isNotNull(baseManager.checkMenuExistRole(id));
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenuDto> menus) {
        return RouteUtils.buildMenus(menus);
    }
}
