package com.xueyi.system.authority.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.authority.service.ISysMenuService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends TreeController<SysMenuDto, ISysMenuService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "菜单";
    }

    /**
     * 获取当前节点及其祖籍信息
     */
    @InnerAuth
    @GetMapping("/inner/{id}")
    public R<SysMenuDto> getInfoInner(@PathVariable Serializable id) {
        return R.ok(baseService.selectById(id));
    }

    /**
     * 查询菜单列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions("authority:menu:list")
    public AjaxResult list(SysMenuDto sysMenu) {
        return super.list(sysMenu);
    }

    /**
     * 查询菜单列表（排除节点）
     */
    @GetMapping("/list/exclude")
    @RequiresPermissions("authority:menu:list")
    public AjaxResult listExNodes(SysMenuDto sysMenu) {
        return super.listExNodes(sysMenu);
    }

    /**
     * 查询菜单详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions("authority:menu:single")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 获取路由信息
     */
    @GetMapping("/getRouters/{moduleId}")
    public AjaxResult getRouters(@PathVariable Long moduleId) {
        List<SysMenuDto> menus = baseService.getRoutes(moduleId);
        return AjaxResult.success(baseService.buildMenus(TreeUtils.buildTree(menus)));
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集
     */
    @PostMapping("/routeList")
    public AjaxResult getMenuByMenuType(@RequestBody SysMenuDto menu) {
        if (ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集（排除节点）
     */
    @PostMapping("/routeList/exclude")
    public AjaxResult getMenuByMenuTypeExNodes(@RequestBody SysMenuDto menu) {
        if (ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        Iterator<SysMenuDto> it = menus.iterator();
        while (it.hasNext()) {
            SysMenuDto next = (SysMenuDto) it.next();
            if (ObjectUtil.equals(next.getId(), menu.getId()) ||
                    ArrayUtils.contains(StringUtils.split(next.getAncestors(), ","), menu.getId() + ""))
                it.remove();
        }
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 菜单新增
     */
    @Override
    @PostMapping
    @RequiresPermissions("authority:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysMenuDto sysMenu) {
        return super.add(sysMenu);
    }

    /**
     * 菜单修改
     */
    @Override
    @PutMapping
    @RequiresPermissions("authority:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysMenuDto sysMenu) {
        return super.edit(sysMenu);
    }

    /**
     * 菜单修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {"authority:menu:edit", "authority:menu:editStatus"}, logical = Logical.OR)
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@Validated @RequestBody SysMenuDto sysMenu) {
        return super.editStatus(sysMenu);
    }

    /**
     * 菜单批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions("authority:menu:delete")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysMenuDto menu) {
        if(ObjectUtil.equals(menu.getId(), AuthorityConstants.MENU_TOP_NODE))
            throw new ServiceException("默认菜单不允许修改！");
        if (baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，菜单名称已存在", operate.getInfo(), getNodeName(), menu.getName()));
        else if (SecurityUtils.isNotAdminTenant()) {
            if (operate.isAdd()) {
                if (StringUtils.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), menu.getIsCommon()))
                    throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限", operate.getInfo(), getNodeName(), menu.getName()));
            } else {
                SysMenuDto original = baseService.selectById(menu.getId());
                if (ObjectUtil.isNull(original))
                    throw new ServiceException("数据不存在");
                else if (StringUtils.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), original.getIsCommon()))
                    throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限", operate.getInfo(), getNodeName(), menu.getName()));
            }
        }
    }

    /**
     * 前置校验 （强制）删除
     * 必须满足内容
     *
     * @param operate 操作类型
     * @param idList  Id集合
     */
    @Override
    protected void baseRemoveValidated(BaseConstants.Operate operate, List<Long> idList) {
        // remove top node
        for (int i = idList.size() - 1; i >= 0; i--)
            if (ObjectUtil.equals(idList.get(i), AuthorityConstants.MENU_TOP_NODE))
                idList.remove(i);
        if (CollUtil.isEmpty(idList))
            throw new ServiceException(StrUtil.format("删除失败，无法删除默认{}！", getNodeName()));
    }
}
