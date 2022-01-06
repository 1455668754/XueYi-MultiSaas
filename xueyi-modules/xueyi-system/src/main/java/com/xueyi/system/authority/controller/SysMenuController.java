package com.xueyi.system.authority.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.domain.authority.dto.SysMenuDto;
import com.xueyi.system.authority.service.ISysMenuService;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResult getMenuByMenuType( @RequestBody SysMenuDto menu) {
        if(ObjectUtil.isNull(menu) || ObjectUtil.isNull(menu.getModuleId()) || ObjectUtil.isNull(menu.getMenuType()))
            throw new ServiceException("请传入有效参数");
        List<SysMenuDto> menus = baseService.getMenuByMenuType(menu.getModuleId(), menu.getMenuType());
        return AjaxResult.success(TreeUtils.buildTree((menus)));
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysMenuDto menu) {
        if (baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，菜单名称已存在", operate.getInfo(), getNodeName(), menu.getName()));
        else if (!SecurityUtils.isAdminTenant()) {
            if (BaseConstants.Operate.ADD == operate || BaseConstants.Operate.ADD_FORCE == operate) {
                if (StringUtils.equals(AuthorityConstants.IsCommon.YES.getCode(), menu.getIsCommon()))
                    throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限", operate.getInfo(), getNodeName(), menu.getName()));
            } else {
                SysMenuDto original = baseService.selectById(menu.getId());
                if (ObjectUtil.isNull(original))
                    throw new ServiceException("数据不存在");
                else if (StringUtils.equals(AuthorityConstants.IsCommon.YES.getCode(), original.getIsCommon()))
                    throw new ServiceException(StrUtil.format("{}{}{}失败，无操作权限", operate.getInfo(), getNodeName(), menu.getName()));
            }
        }
    }
}
