package com.xueyi.system.utils.cloud;

import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.utils.cloud.route.CMetaVo;
import com.xueyi.system.utils.cloud.route.CRouterVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由树工具类
 *
 * @author xueyi
 */
public class CRouteUtils {

    /** 面包屑导航中不可被点击标识 */
    private static final String NO_REDIRECT = "noRedirect";

    /** 路由树初始深度 */
    private static final int LEVEL_0 = 0;

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public static List<CRouterVo> buildMenus(List<SysMenuDto> menus) {
        SysMenuDto menu = new SysMenuDto();
        menu.setFullPath(StrUtil.EMPTY);
        menu.setChildren(menus);
        return recursionFn(menu, LEVEL_0);
    }

    /**
     * 递归菜单列表
     *
     * @param menus 菜单列表
     * @param level 路由树深度
     * @return 路由树
     */
    private static List<CRouterVo> recursionFn(SysMenuDto menus, int level) {
        List<CRouterVo> routers = new ArrayList<>();
        if (CollUtil.isNotEmpty(menus.getChildren())) {
            CRouterVo router;
            for (SysMenuDto menu : menus.getChildren()) {
                if (level == LEVEL_0 && menu.isDetails()) {
                    router = new CRouterVo();
                    getRoute(menu, router);
                    routers.add(router);
                }
                menu.setFullPath(menus.getFullPath() + StrUtil.SLASH + menu.getPath());
                if (CollUtil.isNotEmpty(menu.getChildren()))
                    assembleDetails(menu, routers);
                if (!menu.isDetails()) {
                    router = new CRouterVo();
                    if (CollUtil.isNotEmpty(menu.getChildren())) {
                        router.setChildren(recursionFn(menu, ++level));
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
     * @param menu    菜单对象
     * @param routers 路由列表
     */
    private static void assembleDetails(SysMenuDto menu, List<CRouterVo> routers) {
        CRouterVo router;
        for (SysMenuDto detailsMenu : menu.getChildren()) {
            if (detailsMenu.isDetails()) {
                detailsMenu.setCurrentActiveMenu(menu.getFullPath());
                router = new CRouterVo();
                // 详情型菜单上移一级
                detailsMenu.setParentId(menu.getParentId());
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
    private static void getRoute(SysMenuDto menu, CRouterVo router) {
        router.setMeta(getMeta(menu));
        router.setPath(getRouterPath(menu));
        router.setName(menu.getName());
        router.setQuery(menu.getParamPath());
        router.setHidden(StrUtil.equals(DictConstants.DicShowHide.HIDE.getCode(), menu.getHideMenu()));
        router.setComponent(getComponent(menu));
        if (menu.isDir() && CollUtil.isNotEmpty(menu.getChildren())) {
            router.setAlwaysShow(true);
            router.setRedirect(NO_REDIRECT);
        }
    }

    /**
     * 获取路由元信息
     *
     * @param menu 菜单信息
     * @return 路由元信息
     */
    private static CMetaVo getMeta(SysMenuDto menu) {
        CMetaVo meta = new CMetaVo();
        meta.setTitle(menu.getTitle());
        meta.setIcon(menu.getIcon());
        meta.setNoCache(!StrUtil.equals(DictConstants.DicYesNo.YES.getCode(), menu.getIsCache()));
        if (menu.isEmbedded())
            meta.setLink(menu.getFrameSrc());
        if (menu.isDetails()) {
            meta.setActiveMenu(menu.getCurrentActiveMenu());
        }
        meta.setAffix(StrUtil.equals(DictConstants.DicYesNo.YES.getCode(), menu.getIsAffix()));
        meta.setBreadcrumb(!StrUtil.equals(DictConstants.DicShowHide.HIDE.getCode(), menu.getHideBreadcrumb()));
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
            return StrUtil.SLASH + menu.getPath();
        return menu.getPath();
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    private static String getComponent(SysMenuDto menu) {
        return ObjectUtil.equals(AuthorityConstants.MENU_TOP_NODE, menu.getParentId()) || menu.isExternalLinks()
                ? ComponentType.LAYOUT.getCode()
                : menu.isEmbedded()
                ? ComponentType.IFRAME.getCode()
                : isParentView(menu) ? ComponentType.PARENT_VIEW.getCode() : menu.getComponent();
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isParentView(SysMenuDto menu) {
        return CollUtil.isNotEmpty(menu.getChildren()) && menu.isDir();
    }

    /** 组件标识 */
    private enum ComponentType {

        LAYOUT("Layout"),
        PARENT_VIEW("ParentView"),
        IFRAME("InnerLink");

        private final String code;

        ComponentType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
