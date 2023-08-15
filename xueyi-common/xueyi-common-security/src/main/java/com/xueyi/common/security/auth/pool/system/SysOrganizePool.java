package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 组织模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysOrganizePool {

    /** 系统服务 | 组织模块 | 部门管理 | 列表 */
    String SYS_DEPT_LIST = "organize:dept:list";
    /** 系统服务 | 组织模块 | 部门管理 | 详情 */
    String SYS_DEPT_SINGLE = "organize:dept:single";
    /** 系统服务 | 组织模块 | 部门管理 | 新增 */
    String SYS_DEPT_ADD = "organize:dept:add";
    /** 系统服务 | 组织模块 | 部门管理 | 修改 */
    String SYS_DEPT_EDIT = "organize:dept:edit";
    /** 系统服务 | 组织模块 | 部门管理 | 权限 */
    String SYS_DEPT_AUTH = "organize:dept:auth";
    /** 系统服务 | 组织模块 | 部门管理 | 修改状态 */
    String SYS_DEPT_ES = "organize:dept:es";
    /** 系统服务 | 组织模块 | 部门管理 | 删除 */
    String SYS_DEPT_DEL = "organize:dept:delete";

    /** 系统服务 | 组织模块 | 岗位管理 | 列表 */
    String SYS_POST_LIST = "organize:post:list";
    /** 系统服务 | 组织模块 | 岗位管理 | 详情 */
    String SYS_POST_SINGLE = "organize:post:single";
    /** 系统服务 | 组织模块 | 岗位管理 | 新增 */
    String SYS_POST_ADD = "organize:post:add";
    /** 系统服务 | 组织模块 | 岗位管理 | 修改 */
    String SYS_POST_EDIT = "organize:post:edit";
    /** 系统服务 | 组织模块 | 岗位管理 | 权限 */
    String SYS_POST_AUTH = "organize:post:auth";
    /** 系统服务 | 组织模块 | 岗位管理 | 修改状态 */
    String SYS_POST_ES = "organize:post:es";
    /** 系统服务 | 组织模块 | 岗位管理 | 删除 */
    String SYS_POST_DEL = "organize:post:delete";

    /** 系统服务 | 组织模块 | 用户管理 | 列表 */
    String SYS_USER_LIST = "organize:user:list";
    /** 系统服务 | 组织模块 | 用户管理 | 详情 */
    String SYS_USER_SINGLE = "organize:user:single";
    /** 系统服务 | 组织模块 | 用户管理 | 新增 */
    String SYS_USER_ADD = "organize:user:add";
    /** 系统服务 | 组织模块 | 用户管理 | 修改 */
    String SYS_USER_EDIT = "organize:user:edit";
    /** 系统服务 | 组织模块 | 用户管理 | 权限 */
    String SYS_USER_AUTH = "organize:user:auth";
    /** 系统服务 | 组织模块 | 用户管理 | 修改状态 */
    String SYS_USER_ES = "organize:user:es";
    /** 系统服务 | 组织模块 | 用户管理 | 密码修改 */
    String SYS_USER_RESET_PASSWORD = "organize:user:rp";
    /** 系统服务 | 组织模块 | 用户管理 | 删除 */
    String SYS_USER_DEL = "organize:user:delete";
    /** 系统服务 | 组织模块 | 用户管理 | 导入 */
    String SYS_USER_IMPORT = "organize:user:import";
    /** 系统服务 | 组织模块 | 用户管理 | 导出 */
    String SYS_USER_EXPORT = "organize:user:export";
}
