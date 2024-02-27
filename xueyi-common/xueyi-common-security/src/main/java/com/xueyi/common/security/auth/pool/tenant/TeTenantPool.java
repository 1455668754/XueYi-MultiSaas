package com.xueyi.common.security.auth.pool.tenant;

/**
 * 租户服务 | 租户模块 权限标识常量
 *
 * @author xueyi
 */
public interface TeTenantPool {

    /** 租户服务 | 租户模块 | 租户管理 | 列表 */
    String TE_TENANT_LIST = "RD:tenant:tenant:tenant:list";
    /** 租户服务 | 租户模块 | 租户管理 | 详情 */
    String TE_TENANT_SINGLE = "RD:tenant:tenant:tenant:single";
    /** 租户服务 | 租户模块 | 租户管理 | 新增 */
    String TE_TENANT_ADD = "RD:tenant:tenant:tenant:add";
    /** 租户服务 | 租户模块 | 租户管理 | 修改 */
    String TE_TENANT_EDIT = "RD:tenant:tenant:tenant:edit";
    /** 租户服务 | 租户模块 | 租户管理 | 权限 */
    String TE_TENANT_AUTH = "RD:tenant:tenant:tenant:auth";
    /** 租户服务 | 租户模块 | 租户管理 | 修改状态 */
    String TE_TENANT_ES = "RD:tenant:tenant:tenant:es";
    /** 租户服务 | 租户模块 | 租户管理 | 删除 */
    String TE_TENANT_DEL = "RD:tenant:tenant:tenant:del";

}
