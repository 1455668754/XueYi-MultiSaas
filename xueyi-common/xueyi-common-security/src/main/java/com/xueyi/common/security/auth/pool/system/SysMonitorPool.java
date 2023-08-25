package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 监控模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysMonitorPool {

    /** 系统服务 | 监控模块 | 访问日志管理 | 列表 */
    String SYS_LOGIN_LOG_LIST = "monitor:loginLog:list";
    /** 系统服务 | 监控模块 | 访问日志管理 | 删除 */
    String SYS_LOGIN_LOG_DEL = "monitor:loginLog:delete";
    /** 系统服务 | 监控模块 | 访问日志管理 | 导出 */
    String SYS_LOGIN_LOG_EXPORT = "monitor:loginLog:export";

    /** 系统服务 | 监控模块 | 操作日志管理 | 列表 */
    String SYS_OPERATE_LOG_LIST = "monitor:operateLog:list";
    /** 系统服务 | 监控模块 | 操作日志管理 | 详情 */
    String SYS_OPERATE_LOG_SINGLE = "monitor:operateLog:single";
    /** 系统服务 | 监控模块 | 操作日志管理 | 删除 */
    String SYS_OPERATE_LOG_DEL = "monitor:operateLog:delete";
    /** 系统服务 | 监控模块 | 操作日志管理 | 导出 */
    String SYS_OPERATE_LOG_EXPORT = "monitor:operateLog:export";

    /** 系统服务 | 监控模块 | 在线用户管理 | 列表 */
    String SYS_ONLINE_LIST = "monitor:online:list";
    /** 系统服务 | 监控模块 | 在线用户管理 | 强退 */
    String SYS_ONLINE_FORCE_LOGOUT = "monitor:online:forceLogout";
}
