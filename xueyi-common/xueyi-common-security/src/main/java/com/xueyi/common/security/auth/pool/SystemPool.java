package com.xueyi.common.security.auth.pool;

import com.xueyi.common.security.auth.pool.system.SysApplicationPool;
import com.xueyi.common.security.auth.pool.system.SysAuthorityPool;
import com.xueyi.common.security.auth.pool.system.SysDictPool;
import com.xueyi.common.security.auth.pool.system.SysMonitorPool;
import com.xueyi.common.security.auth.pool.system.SysNoticePool;
import com.xueyi.common.security.auth.pool.system.SysOrganizePool;

/**
 * 系统服务 权限标识常量
 *
 * @author xueyi
 */
public interface SystemPool extends SysApplicationPool, SysAuthorityPool, SysDictPool, SysMonitorPool, SysNoticePool, SysOrganizePool {


    /** 系统 - 文件管理 - 列表 */
    String SYS_FILE_LIST = "system:file:list";
    /** 系统 - 文件管理 - 详情 */
    String SYS_FILE_SINGLE = "system:file:single";
    /** 系统 - 文件管理 - 新增 */
    String SYS_FILE_ADD = "system:file:add";
    /** 系统 - 文件管理 - 删除 */
    String SYS_FILE_DEL = "system:file:delete";
}
