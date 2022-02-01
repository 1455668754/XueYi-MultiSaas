package com.xueyi.system.authority.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.InnerAuth;
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
     * 查询菜单列表
     */
    @Override
    @GetMapping("/list")
    public AjaxResult list(SysMenuDto menu) {
        return super.list(menu);
    }

    /**
     * 查询菜单列表（排除节点）
     */
    @Override
    @GetMapping("/list/exclude")
    public AjaxResult listExNodes(SysMenuDto menu) {
        return super.listExNodes(menu);
    }

    /**
     * 查询菜单详细
     */
    @Override
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }
    
    /**
     * 获取当前节点及其祖籍信息
     */
    @InnerAuth
    @GetMapping("/getAncestorsList/{id}")
    public R<List<SysMenuDto>> getAncestorsList(@PathVariable("id") Long id) {
        return R.ok(baseService.selectAncestorsListById(id));
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
        if(ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 根据菜单类型获取指定模块的可配菜单集（排除节点）
     */
    @PostMapping("/routeList/exclude")
    public AjaxResult getMenuByMenuTypeExNodes(@RequestBody SysMenuDto menu) {
        if(ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        Iterator<SysMenuDto> it = menus.iterator();
        while (it.hasNext()) {
            SysMenuDto next = (SysMenuDto) it.next();
            if (ObjectUtil.equals(next.getId(),menu.getId()) ||
                    ArrayUtils.contains(StringUtils.split(next.getAncestors(), ","), menu.getId() + ""))
                it.remove();
        }
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 菜单新增
     */
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenuDto menu) {
        return super.add(menu);
    }

    /**
     * 菜单修改
     */
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenuDto menu) {
        return super.edit(menu);
    }

    /**
     * 菜单修改状态
     */
    @PutMapping("/status")
    public AjaxResult editStatus(@Validated @RequestBody SysMenuDto menu) {
        return super.editStatus(menu);
    }

    /**
     * 菜单删除
     */
    @DeleteMapping("/batch/{idList}")
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysMenuDto menu) {
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
}
