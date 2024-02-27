package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 权限模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysAuthorityPool {

    /** 系统服务 | 权限模块 | 模块管理 | 列表 */
    String SYS_MODULE_LIST = "RD:system:authority:module:list";
    /** 系统服务 | 权限模块 | 模块管理 | 详情 */
    String SYS_MODULE_SINGLE = "RD:system:authority:module:single";
    /** 系统服务 | 权限模块 | 模块管理 | 新增 */
    String SYS_MODULE_ADD = "RD:system:authority:module:add";
    /** 系统服务 | 权限模块 | 模块管理 | 修改 */
    String SYS_MODULE_EDIT = "RD:system:authority:module:edit";
    /** 系统服务 | 权限模块 | 模块管理 | 状态修改 */
    String SYS_MODULE_ES = "RD:system:authority:module:es";
    /** 系统服务 | 权限模块 | 模块管理 | 删除 */
    String SYS_MODULE_DEL = "RD:system:authority:module:del";

    /** 系统服务 | 权限模块 | 菜单管理 | 列表 */
    String SYS_MENU_LIST = "RD:system:authority:menu:list";
    /** 系统服务 | 权限模块 | 菜单管理 | 详情 */
    String SYS_MENU_SINGLE = "RD:system:authority:menu:single";
    /** 系统服务 | 权限模块 | 菜单管理 | 新增 */
    String SYS_MENU_ADD = "RD:system:authority:menu:add";
    /** 系统服务 | 权限模块 | 菜单管理 | 修改 */
    String SYS_MENU_EDIT = "RD:system:authority:menu:edit";
    /** 系统服务 | 权限模块 | 菜单管理 | 状态修改 */
    String SYS_MENU_ES = "RD:system:authority:menu:es";
    /** 系统服务 | 权限模块 | 菜单管理 | 删除 */
    String SYS_MENU_DEL = "RD:system:authority:menu:del";

    /** 系统服务 | 权限模块 | 角色管理 | 列表 */
    String SYS_ROLE_LIST = "RD:system:authority:role:list";
    /** 系统服务 | 权限模块 | 角色管理 | 详情 */
    String SYS_ROLE_SINGLE = "RD:system:authority:role:single";
    /** 系统服务 | 权限模块 | 角色管理 | 新增 */
    String SYS_ROLE_ADD = "RD:system:authority:role:add";
    /** 系统服务 | 权限模块 | 角色管理 | 修改 */
    String SYS_ROLE_EDIT = "RD:system:authority:role:edit";
    /** 系统服务 | 权限模块 | 角色管理 | 权限 */
    String SYS_ROLE_AUTH = "RD:system:authority:role:auth";
    /** 系统服务 | 权限模块 | 角色管理 | 状态修改 */
    String SYS_ROLE_ES = "RD:system:authority:role:es";
    /** 系统服务 | 权限模块 | 角色管理 | 删除 */
    String SYS_ROLE_DEL = "RD:system:authority:role:del";

    /** 系统服务 | 权限模块 | 租户权限组管理 | 列表 */
    String SYS_AUTH_GROUP_LIST = "RD:system:authority:authGroup:list";
    /** 系统服务 | 权限模块 | 租户权限组管理 | 详情 */
    String SYS_AUTH_GROUP_SINGLE = "RD:system:authority:authGroup:single";
    /** 系统服务 | 权限模块 | 租户权限组管理 | 新增 */
    String SYS_AUTH_GROUP_ADD = "RD:system:authority:authGroup:add";
    /** 系统服务 | 权限模块 | 租户权限组管理 | 修改 */
    String SYS_AUTH_GROUP_EDIT = "RD:system:authority:authGroup:edit";
    /** 系统服务 | 权限模块 | 租户权限组管理 | 状态修改 */
    String SYS_AUTH_GROUP_ES = "RD:system:authority:authGroup:es";
    /** 系统服务 | 权限模块 | 租户权限组管理 | 删除 */
    String SYS_AUTH_GROUP_DEL = "RD:system:authority:authGroup:del";
}
