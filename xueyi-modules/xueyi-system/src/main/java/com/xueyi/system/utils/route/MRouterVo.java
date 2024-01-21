package com.xueyi.system.utils.route;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 路由配置信息
 *
 * @author xueyi
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MRouterVo {

    /** 路由名字 */
    private String name;

    /** 路由地址 */
    private String path;

    /** 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击 */
    private String redirect;  // 暂无

    /** 组件地址 */
    private String component;

    /** 是否禁用 */
    private Boolean disabled;

    /** 菜单标签设置 */
    private MTagVo tag;

    /** 其他元素 */
    private MMetaVo meta;

    /** 子路由 */
    private List<MRouterVo> children;

}
