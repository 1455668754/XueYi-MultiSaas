package com.xueyi.common.security.auth.pool.tenant;

/**
 * 租户服务 | 租户模块 权限标识常量
 *
 * @author xueyi
 */
public interface TeTenantPool {

    /** 租户服务 | 租户模块 | 租户管理 | 列表 */
    String TE_TENANT_LIST = "tenant:tenant:list";
    /** 租户服务 | 租户模块 | 租户管理 | 详情 */
    String TE_TENANT_SINGLE = "tenant:tenant:single";
    /** 租户服务 | 租户模块 | 租户管理 | 新增 */
    String TE_TENANT_ADD = "tenant:tenant:add";
    /** 租户服务 | 租户模块 | 租户管理 | 修改 */
    String TE_TENANT_EDIT = "tenant:tenant:edit";
    /** 租户服务 | 租户模块 | 租户管理 | 权限 */
    String TE_TENANT_AUTH = "tenant:tenant:auth";
    /** 租户服务 | 租户模块 | 租户管理 | 修改状态 */
    String TE_TENANT_ES = "tenant:tenant:es";
    /** 租户服务 | 租户模块 | 租户管理 | 删除 */
    String TE_TENANT_DEL = "tenant:tenant:delete";
    /** 租户服务 | 租户模块 | 租户管理 | 导入 */
    String TE_TENANT_IMPORT = "tenant:tenant:import";
    /** 租户服务 | 租户模块 | 租户管理 | 导出 */
    String TE_TENANT_EXPORT = "tenant:tenant:export";

}
