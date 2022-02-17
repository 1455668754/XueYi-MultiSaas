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
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ISysModuleService moduleService;

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
    @RequiresPermissions(Auth.SYS_MENU_LIST)
    public AjaxResult listExtra(SysMenuDto menu) {
        return super.listExtra(menu);
    }

    /**
     * 查询菜单列表（排除节点）
     */
    @GetMapping("/list/exclude")
    @RequiresPermissions(Auth.SYS_MENU_LIST)
    public AjaxResult listExNodes(SysMenuDto menu) {
        return super.listExNodes(menu);
    }

    /**
     * 查询菜单详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_MENU_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
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
                    ArrayUtils.contains(StringUtils.split(next.getAncestors(), StrUtil.COMMA), menu.getId() + StrUtil.EMPTY))
                it.remove();
        }
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 菜单新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_MENU_ADD)
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysMenuDto menu) {
        return super.add(menu);
    }

    /**
     * 菜单修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_MENU_EDIT)
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysMenuDto menu) {
        return super.edit(menu);
    }

    /**
     * 菜单修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_MENU_EDIT, Auth.SYS_MENU_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysMenuDto menu) {
        return super.editStatus(menu);
    }

    /**
     * 菜单批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_MENU_DELETE)
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
        SysMenuDto menu = new SysMenuDto();
        menu.setStatus(BaseConstants.Status.NORMAL.getCode());
        return AjaxResult.success(TreeUtils.buildTree(baseService.selectList(menu)));
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysMenuDto menu) {
        if (ObjectUtil.equals(menu.getId(), AuthorityConstants.MENU_TOP_NODE))
            throw new ServiceException("默认菜单不允许修改！");
        if (baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，菜单名称已存在", operate.getInfo(), getNodeName(), menu.getTitle()));
        if(operate.isAdd() && SecurityUtils.isNotAdminTenant() && StringUtils.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), menu.getIsCommon())){
            throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
        }
        if(operate.isEdit()){
            SysMenuDto original = baseService.selectById(menu.getId());
            if (ObjectUtil.isNull(original))
                throw new ServiceException("数据不存在");
            if (SecurityUtils.isNotAdminTenant()) {
                if (StringUtils.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), original.getIsCommon())) {
                    throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
                }
            }
            if(!StringUtils.equals(menu.getIsCommon(), original.getIsCommon())) {
                throw new ServiceException(StrUtil.format("{}{}{}失败，公共菜单属性禁止变更！", operate.getInfo(), getNodeName(), menu.getTitle()));
            }
        }
        if (StringUtils.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), menu.getIsCommon())) {
            SysModuleDto module = moduleService.selectById(menu.getModuleId());
            if (StringUtils.equals(DictConstants.DicCommonPrivate.PRIVATE.getCode(), module.getIsCommon()))
                throw new ServiceException(StrUtil.format("{}{}{}失败，公共菜单必须挂载在公共模块下！", operate.getInfo(), getNodeName(), menu.getTitle()));
            if(operate.isEdit()){
                SysMenuDto original = baseService.selectById(menu.getId());
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
