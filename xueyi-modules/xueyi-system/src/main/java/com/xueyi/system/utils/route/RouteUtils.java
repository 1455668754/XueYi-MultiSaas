package com.xueyi.system.utils.route;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.system.api.domain.authority.dto.SysMenuDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由树工具类
 *
 * @author xueyi
 */
public class RouteUtils {

    private static final int DYNAMIC_LEVEL = 5;

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public static List<RouterVo> buildMenus(List<SysMenuDto> menus) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenuDto menu : menus) {
            RouterVo router;
            if (StrUtil.equals(AuthorityConstants.MenuType.DETAILS.getCode(), menu.getMenuType())) {
                router = new RouterVo();
                getRoute(menu, router);
                routers.add(router);
            }
            if (CollUtil.isNotEmpty(menu.getChildren()))
                assembleDetails(menu.getChildren(), routers, menu.getParentId());
        }
        routers.addAll(recursionFn(menus));
        return routers;
    }

    /**
     * 递归菜单列表
     *
     * @param menus      菜单列表
     * @return 路由树
     */
    private static List<RouterVo> recursionFn(List<SysMenuDto> menus) {
        List<RouterVo> routers = new ArrayList<>();
        if (CollUtil.isNotEmpty(menus)) {
            RouterVo router;
            for (SysMenuDto menu : menus) {
                if (CollUtil.isNotEmpty(menu.getChildren()))
                    assembleDetails(menu.getChildren(), routers, menu.getParentId());
                if (!StrUtil.equals(AuthorityConstants.MenuType.DETAILS.getCode(), menu.getMenuType())) {
                    router = new RouterVo();
                    if (CollUtil.isNotEmpty(menu.getChildren())) {
                        router.setChildren(recursionFn(menu.getChildren()));
                    }
                    getRoute(menu, router);
                    routers.add(router);
                }
            }
        }
        return routers;
    }

    /**
     * 组装详情列表
     *
     * @param menus      菜单列表
     * @param routers 路由列表
     * @param parentId 父级Id
     */
    private static void assembleDetails(List<SysMenuDto> menus, List<RouterVo> routers,Long parentId) {
        RouterVo router;
        for (SysMenuDto detailsMenu : menus) {
            if (StrUtil.equals(AuthorityConstants.MenuType.DETAILS.getCode(), detailsMenu.getMenuType())) {
                router = new RouterVo();
                // 详情型菜单上移一级
                detailsMenu.setParentId(parentId);
                getRoute(detailsMenu, router);
                routers.add(router);
            }
        }
    }

    /**
     * 获取路由信息
     *
     * @param menu   菜单信息
     * @param router 路由信息
     */
    private static void getRoute(SysMenuDto menu, RouterVo router) {
        router.setMeta(getMeta(menu));
        router.setPath(getRouterPath(menu));
        router.setName(menu.getName());
        router.setDisabled(StrUtil.equals(AuthorityConstants.Status.YES.getCode(), menu.getIsDisabled()));
        router.setParamPath(menu.getParamPath());
        router.setComponent(getComponent(menu));
    }

    /**
     * 获取菜单标签信息
     *
     * @param menu 菜单信息
     * @return 菜单标签信息
     */
    private static TagVo getTag(SysMenuDto menu) {
        TagVo tag = new TagVo();
        return tag;
    }

    /**
     * 获取路由元信息
     *
     * @param menu 菜单信息
     * @return 路由元信息
     */
    private static MetaVo getMeta(SysMenuDto menu) {
        MetaVo meta = new MetaVo();
        meta.setTitle(menu.getTitle());
        meta.setIcon(menu.getIcon());
        if (StrUtil.equals(AuthorityConstants.MenuType.DETAILS.getCode(), menu.getMenuType())) {
            meta.setDynamicLevel(DYNAMIC_LEVEL);
            meta.setRealPath(menu.getRealPath());
            meta.setCurrentActiveMenu(menu.getCurrentActiveMenu());
        }
        // ?
        meta.setIgnoreKeepAlive(StrUtil.equals(AuthorityConstants.Status.YES.getCode(), menu.getIsCache()));
        meta.setAffix(StrUtil.equals(AuthorityConstants.Status.YES.getCode(), menu.getIsAffix()));
        if (StrUtil.equals(AuthorityConstants.FrameType.EMBEDDED.getCode(), menu.getFrameType()))
            meta.setFrameSrc(menu.getFrameSrc());
        meta.setTransitionName(menu.getTransitionName());
        meta.setHideBreadcrumb(StrUtil.equals(AuthorityConstants.Hide.YES.getCode(), menu.getHideBreadcrumb()));
        if (StrUtil.isNotEmpty(menu.getParamPath()))
            meta.setCarryParam(true);
        meta.setHideTab(StrUtil.equals(AuthorityConstants.Hide.YES.getCode(), menu.getHideTab()));
        meta.setHideMenu(StrUtil.equals(AuthorityConstants.Hide.YES.getCode(), menu.getHideMenu()));
        meta.setHideChildrenInMenu(StrUtil.equals(AuthorityConstants.Hide.YES.getCode(), menu.getHideChildren()));
        if (StrUtil.equals(AuthorityConstants.FrameType.EXTERNAL_LINKS.getCode(), menu.getFrameType()))
            meta.setLink(true);
        meta.setOrderNo(menu.getSort());
        meta.setIgnoreRoute(StrUtil.equals(AuthorityConstants.Status.YES.getCode(), menu.getIgnoreRoute()));
        meta.setHidePathForChildren(StrUtil.equals(AuthorityConstants.Hide.YES.getCode(), menu.getHidePathForChildren()));
        return meta;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    private static String getRouterPath(SysMenuDto menu) {
        // 外链方式
        if (StrUtil.equals(AuthorityConstants.FrameType.EXTERNAL_LINKS.getCode(), menu.getFrameType()))
            return menu.getFrameSrc();
        // 一级目录
        if (ObjectUtil.equals(AuthorityConstants.MENU_TOP_NODE, menu.getParentId()))
            return "/" + menu.getPath();
        return menu.getPath();
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    private static String getComponent(SysMenuDto menu) {
        if (ObjectUtil.equals(AuthorityConstants.MENU_TOP_NODE, menu.getParentId())
                || StrUtil.equals(AuthorityConstants.FrameType.EXTERNAL_LINKS.getCode(), menu.getFrameType()))
            return AuthorityConstants.ComponentType.LAYOUT.getCode();
        return menu.getComponent();
    }
}
