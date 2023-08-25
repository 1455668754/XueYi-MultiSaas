package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 消息模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysNoticePool {

    /** 系统服务 | 消息模块 | 通知公告管理 | 列表 */
    String SYS_NOTICE_LIST = "system:notice:list";
    /** 系统服务 | 消息模块 | 通知公告管理 | 详情 */
    String SYS_NOTICE_SINGLE = "system:notice:single";
    /** 系统服务 | 消息模块 | 通知公告管理 | 新增 */
    String SYS_NOTICE_ADD = "system:notice:add";
    /** 系统服务 | 消息模块 | 通知公告管理 | 修改 */
    String SYS_NOTICE_EDIT = "system:notice:edit";
    /** 系统服务 | 消息模块 | 通知公告管理 | 删除 */
    String SYS_NOTICE_DEL = "system:notice:delete";

}
