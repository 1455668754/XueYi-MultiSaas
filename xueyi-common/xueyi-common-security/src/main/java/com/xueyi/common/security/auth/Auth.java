package com.xueyi.common.security.auth;

import com.xueyi.common.security.auth.pool.GenPool;
import com.xueyi.common.security.auth.pool.JobPool;
import com.xueyi.common.security.auth.pool.SystemPool;
import com.xueyi.common.security.auth.pool.TenantPool;

/**
 * Token 权限标识常量
 *
 * @author xueyi
 */
public class Auth implements SystemPool, JobPool, GenPool, TenantPool {
}
