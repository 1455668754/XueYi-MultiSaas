package com.xueyi.system.authority.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.service.ISysMenuService;

/**
 * 系统服务 | 权限模块 | 菜单管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysMenuController extends TreeController<SysMenuQuery, SysMenuDto, ISysMenuService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "菜单";
    }

    /**
     * 构造树型Top节点
     *
     * @param query 数据查询对象
     * @return Top节点对象
     */
    @Override
    protected SysMenuDto TopNodeBuilder(SysMenuQuery query) {
        SysMenuDto topMenu = super.TopNodeBuilder(query);
        topMenu.setTitle(topMenu.getName());
        topMenu.setFrameType(AuthorityConstants.FrameType.NORMAL.getCode());
        topMenu.setMenuType(AuthorityConstants.MenuType.DIR.getCode());
        topMenu.setModuleId(query.getModuleId());
        return topMenu;
    }

    /**
     * 前置校验 新增/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysMenuDto menu) {
        SecurityContextHolder.setTenantIgnore();
        boolean isNotUnique = baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName());
        SecurityContextHolder.clearTenantIgnore();
        if (isNotUnique) {
            warn(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName()));
        }

        if (menu.isCommon() && SecurityUserUtils.isNotAdminTenant()) {
            warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
        }
    }
}
