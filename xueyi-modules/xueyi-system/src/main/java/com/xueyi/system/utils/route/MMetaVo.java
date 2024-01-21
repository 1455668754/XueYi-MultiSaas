package com.xueyi.system.utils.route;

import lombok.Data;

/**
 * 路由元信息
 *
 * @author xueyi
 */
@Data
public class MMetaVo {

    /** 设置该路由在侧边栏和面包屑中展示的名字 */
    private String title;

    /** 设置该路由的图标 */
    private String icon;

    /** 动态路由可打开Tab页数 */
    private int dynamicLevel;

    /** 动态路由的实际Path, 即去除路由的动态部分 */
    private String realPath;

    /** 是否忽略KeepAlive缓存 */
    private Boolean ignoreKeepAlive;

    /** 是否固定标签 */
    private Boolean affix;

    /** 内嵌iframe的地址 */
    private String frameSrc;

    /** 指定该路由切换的动画名 */
    private String transitionName;

    /** 是否隐藏该路由在面包屑上面的显示 */
    private Boolean hideBreadcrumb;

    /** 该路由是否会携带参数，且需要在tab页上面显示 */
    private Boolean carryParam;

    /** 隐藏所有子菜单 */
    private Boolean hideChildrenInMenu;

    /** 是否为单级菜单 */
    private Boolean single;

    /** 路由参数：如 {"id": 1, "name": "xy"} */
    private String params;

    /** 当前激活的菜单。用于配置详情页时左侧激活的菜单路径 */
    private String currentActiveMenu;

    /** 当前路由是否在标签页显示 */
    private Boolean hideTab;

    /** 当前路由是否在菜单显示 */
    private Boolean hideMenu;

    /** 是否外链 */
    private Boolean isLink;

    /** 菜单排序，只对第一级有效 */
    private Integer orderNo;

    /** 忽略路由。用于在ROUTE_MAPPING以及BACK权限模式下，生成对应的菜单而忽略路由 */
    private Boolean ignoreRoute;

    /** 是否在子级菜单的完整path中忽略本级path */
    private Boolean hidePathForChildren;

}
