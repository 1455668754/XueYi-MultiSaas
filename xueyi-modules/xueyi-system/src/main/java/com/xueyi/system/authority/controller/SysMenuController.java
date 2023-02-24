package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import com.xueyi.system.utils.cloud.CRouteUtils;
import com.xueyi.system.utils.multi.MRouteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends TreeController<SysMenuQuery, SysMenuDto, ISysMenuService> {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysModuleService moduleService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "菜单";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "模块";
    }

    /**
     * 获取当前节点及其祖籍信息 | 内部调用
     */
    @InnerAuth
    @GetMapping("/inner/{id}")
    public R<SysMenuDto> getInfoInner(@PathVariable Serializable id) {
        return R.ok(baseService.selectById(id));
    }

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
            if (ObjectUtil.isNull(menuMap)) menuMap = new HashMap<>();
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

    /**
     * 前置校验 新增/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysMenuDto menu) {
        if (ObjectUtil.equals(menu.getId(), AuthorityConstants.MENU_TOP_NODE))
            warn(StrUtil.format("默认{}不允许修改！", getNodeName()));
        if (baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName()))
            warn(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName()));

        switch (operate) {
            case ADD, ADD_FORCE -> {
            }
            case EDIT, EDIT_FORCE -> {
                SysMenuDto original = baseService.selectById(menu.getId());
                menu.setIsCommon(original.getIsCommon());
                if (ObjectUtil.isNull(original))
                    warn("数据不存在！");
            }
        }

        if (menu.isCommon()) {
            if (SecurityUtils.isNotAdminTenant())
                warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
            SysModuleDto module = moduleService.selectById(menu.getModuleId());
            if (ObjectUtil.isNull(module))
                warn("数据不存在！");
            if (module.isNotCommon())
                warn(StrUtil.format("{}{}{}失败，公共{}必须挂载在公共{}下！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName(), getParentName()));

            SysMenuDto parentMenu = baseService.selectById(menu.getParentId());
            if (ObjectUtil.isNull(parentMenu))
                warn("数据不存在！");
            if (parentMenu.isNotCommon())
                warn(StrUtil.format("{}{}{}失败，公共{}必须挂载在公共{}下！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName(), getNodeName()));
        }
    }

    /**
     * 前置校验 删除
     */
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        List<SysMenuDto> moduleList = baseService.selectListByIds(idList);
        boolean isTenant = SecurityUtils.isAdminTenant();
        Map<Long, SysMenuDto> moduleMap = moduleList.stream().filter(item -> isTenant || item.isNotCommon())
                .collect(Collectors.toMap(SysMenuDto::getId, Function.identity()));
        for (int i = idList.size() - 1; i >= 0; i--)
            if (!moduleMap.containsKey(idList.get(i)) || ObjectUtil.equals(idList.get(i), AuthorityConstants.MENU_TOP_NODE))
                idList.remove(i);
        if (CollUtil.isEmpty(idList))
            warn(StrUtil.format("无待删除{}！", getNodeName()));
    }
}
