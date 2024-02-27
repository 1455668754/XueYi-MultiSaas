package com.xueyi.common.security.auth.pool;

import com.xueyi.common.security.auth.pool.system.SysAuthorityPool;
import com.xueyi.common.security.auth.pool.system.SysDictPool;
import com.xueyi.common.security.auth.pool.system.SysFilePool;
import com.xueyi.common.security.auth.pool.system.SysMonitorPool;
import com.xueyi.common.security.auth.pool.system.SysNoticePool;
import com.xueyi.common.security.auth.pool.system.SysOrganizePool;

/**
 * 系统服务 权限标识常量
 *
 * @author xueyi
 */
public interface SystemPool extends SysAuthorityPool, SysDictPool, SysFilePool, SysMonitorPool, SysNoticePool, SysOrganizePool {
}
