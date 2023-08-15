package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 权限模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysAuthorityPool {

    /** 系统服务 | 权限模块 | 模块管理 | 列表 */
    String SYS_MODULE_LIST = "authority:module:list";
    /** 系统服务 | 权限模块 | 模块管理 | 详情 */
    String SYS_MODULE_SINGLE = "authority:module:single";
    /** 系统服务 | 权限模块 | 模块管理 | 新增 */
    String SYS_MODULE_ADD = "authority:module:add";
    /** 系统服务 | 权限模块 | 模块管理 | 修改 */
    String SYS_MODULE_EDIT = "authority:module:edit";
    /** 系统服务 | 权限模块 | 模块管理 | 修改状态 */
    String SYS_MODULE_ES = "authority:module:es";
    /** 系统服务 | 权限模块 | 模块管理 | 删除 */
    String SYS_MODULE_DEL = "authority:module:delete";

    /** 系统服务 | 权限模块 | 菜单管理 | 列表 */
    String SYS_MENU_LIST = "authority:menu:list";
    /** 系统服务 | 权限模块 | 菜单管理 | 详情 */
    String SYS_MENU_SINGLE = "authority:menu:single";
    /** 系统服务 | 权限模块 | 菜单管理 | 新增 */
    String SYS_MENU_ADD = "authority:menu:add";
    /** 系统服务 | 权限模块 | 菜单管理 | 修改 */
    String SYS_MENU_EDIT = "authority:menu:edit";
    /** 系统服务 | 权限模块 | 菜单管理 | 修改状态 */
    String SYS_MENU_ES = "authority:menu:es";
    /** 系统服务 | 权限模块 | 菜单管理 | 删除 */
    String SYS_MENU_DEL = "authority:menu:delete";

    /** 系统服务 | 权限模块 | 角色管理 | 列表 */
    String SYS_ROLE_LIST = "authority:role:list";
    /** 系统服务 | 权限模块 | 角色管理 | 详情 */
    String SYS_ROLE_SINGLE = "authority:role:single";
    /** 系统服务 | 权限模块 | 角色管理 | 新增 */
    String SYS_ROLE_ADD = "authority:role:add";
    /** 系统服务 | 权限模块 | 角色管理 | 修改 */
    String SYS_ROLE_EDIT = "authority:role:edit";
    /** 系统服务 | 权限模块 | 角色管理 | 权限 */
    String SYS_ROLE_AUTH = "authority:role:auth";
    /** 系统服务 | 权限模块 | 角色管理 | 修改状态 */
    String SYS_ROLE_ES = "authority:role:es";
    /** 系统服务 | 权限模块 | 角色管理 | 删除 */
    String SYS_ROLE_DEL = "authority:role:delete";
}
