package com.xueyi.system.utils.cloud.route;

import lombok.Data;

/**
 * 路由元信息
 *
 * @author xueyi
 */
@Data
public class CMetaVo {

    /** 设置该路由在侧边栏和面包屑中展示的名字 */
    private String title;

    /** 设置该路由的图标，对应路径common/src/assets/icons/svg */
    private String icon;

    /** 设置为true，则不会被 <keep-alive>缓存 */
    private boolean noCache;

    /** 内链地址（http(s)://开头） */
    private String link;

    /** 是否隐藏该路由在面包屑上面的显示 */
    private Boolean breadcrumb;

    /** 是否固定标签 */
    private Boolean affix;

    /** 当前激活的菜单。用于配置详情页时左侧激活的菜单路径 */
    private String activeMenu;

}
