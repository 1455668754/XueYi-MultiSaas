package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 应用模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysApplicationPool {

    /** 系统服务 | 应用模块 | 应用管理 | 列表 */
    String SYS_APPLICATION_LIST = "system:application:list";
    /** 系统服务 | 应用模块 | 应用管理 | 详情 */
    String SYS_APPLICATION_SINGLE = "system:application:single";
    /** 系统服务 | 应用模块 | 应用管理 | 新增 */
    String SYS_APPLICATION_ADD = "system:application:add";
    /** 系统服务 | 应用模块 | 应用管理 | 修改 */
    String SYS_APPLICATION_EDIT = "system:application:edit";
    /** 系统服务 | 应用模块 | 应用管理 | 修改状态 */
    String SYS_APPLICATION_ES = "system:application:es";
    /** 系统服务 | 应用模块 | 应用管理 | 删除 */
    String SYS_APPLICATION_DEL = "system:application:delete";
}
