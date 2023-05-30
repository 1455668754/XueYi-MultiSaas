package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.service.TokenUserService;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.controller.base.BSysMenuController;
import com.xueyi.system.utils.cloud.CRouteUtils;
import com.xueyi.system.utils.multi.MRouteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统服务 | 权限模块 | 菜单管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/menu")
public class SysMenuController extends BSysMenuController {

    @Autowired
    private TokenUserService tokenService;

    /**
     * 获取路由信息
     */
    @GetMapping("/getCloudRouters/{moduleId}")
    public AjaxResult getCloudRouters(@PathVariable Long moduleId) {
        Map<String, Object> menuMap = tokenService.getMenuRoute();
        String moduleKey = ServiceConstants.FromSource.CLOUD.getCode() + moduleId;
        if (ObjectUtil.isNull(menuMap) || ObjectUtil.isNull(menuMap.get(moduleKey))) {
            List<SysMenuDto> menus = baseService.getRoutes(moduleId);
            if (ObjectUtil.isNull(menuMap)) menuMap = new HashMap<>();
            menuMap.put(moduleKey, CRouteUtils.buildMenus(TreeUtil.buildTree(menus)));
            tokenService.setMenuRoute(menuMap);
        }
        return success(menuMap.get(moduleKey));
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/getMultiRouters/{moduleId}")
    public AjaxResult getMultiRouters(@PathVariable Long moduleId) {
        Map<String, Object> menuMap = tokenService.getMenuRoute();
        String moduleKey = ServiceConstants.FromSource.MULTI.getCode() + moduleId;
        if (ObjectUtil.isNull(menuMap) || ObjectUtil.isNull(menuMap.get(moduleKey))) {
            List<SysMenuDto> menus = baseService.getRoutes(moduleId);
            if (ObjectUtil.isNull(menuMap))
                menuMap = new HashMap<>();
            menuMap.put(moduleKey, MRouteUtils.buildMenus(TreeUtil.buildTree(menus)));
            tokenService.setMenuRoute(menuMap);
        }
        return success(menuMap.get(moduleKey));
    }

    /**
     * 查询菜单列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MENU_LIST)")
    public AjaxResult list(SysMenuQuery menu) {
        return super.list(menu);
    }

    /**
     * 查询菜单列表（排除节点）
     */
    @GetMapping("/list/exclude")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MENU_LIST)")
    public AjaxResult listExNodes(SysMenuQuery menu) {
        return super.listExNodes(menu);
    }

    /**
     * 查询菜单详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MENU_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集
     */
    @PostMapping("/routeList")
    public AjaxResult getMenuByMenuType(@RequestBody SysMenuDto menu) {
        if (ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            warn("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        return success(TreeUtil.buildTree((menus)));
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集（排除节点）
     */
    @PostMapping("/routeList/exclude")
    public AjaxResult getMenuByMenuTypeExNodes(@RequestBody SysMenuDto menu) {
        if (ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            warn("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        menus.removeIf(next -> ObjectUtil.equals(next.getId(), menu.getId()) || ArrayUtil.contains(StrUtil.splitToArray(next.getAncestors(), StrUtil.COMMA), menu.getId() + StrUtil.EMPTY));
        return success(TreeUtil.buildTree((menus)));
    }

    /**
     * 菜单新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MENU_ADD)")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysMenuDto menu) {
        return super.add(menu);
    }

    /**
     * 菜单修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MENU_EDIT)")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysMenuDto menu) {
        return super.edit(menu);
    }

    /**
     * 菜单修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_MENU_EDIT, @Auth.SYS_MENU_ES)")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysMenuDto menu) {
        return super.editStatus(menu);
    }

    /**
     * 菜单批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MENU_DEL)")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取菜单选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

}
