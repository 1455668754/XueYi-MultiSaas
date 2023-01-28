package com.xueyi.common.security.auth;

import com.xueyi.common.security.auth.pool.GenPool;
import com.xueyi.common.security.auth.pool.JobPool;
import com.xueyi.common.security.auth.pool.SystemPool;
import com.xueyi.common.security.auth.pool.TenantPool;
import org.springframework.stereotype.Service;

/**
 * Token 权限标识常量
 *
 * @author xueyi
 */
@Service("Auth")
public class Auth implements SystemPool, JobPool, GenPool, TenantPool {
}
