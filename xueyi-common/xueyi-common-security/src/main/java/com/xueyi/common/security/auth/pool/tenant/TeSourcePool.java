package com.xueyi.common.security.auth.pool.tenant;

/**
 * 租户服务 | 策略模块 权限标识常量
 *
 * @author xueyi
 */
public interface TeSourcePool {

    /** 租户服务 | 策略模块 | 数据源管理 | 列表 */
    String TE_SOURCE_LIST = "tenant:source:list";
    /** 租户服务 | 策略模块 | 数据源管理 | 详情 */
    String TE_SOURCE_SINGLE = "tenant:source:single";
    /** 租户服务 | 策略模块 | 数据源管理 | 新增 */
    String TE_SOURCE_ADD = "tenant:source:add";
    /** 租户服务 | 策略模块 | 数据源管理 | 修改 */
    String TE_SOURCE_EDIT = "tenant:source:edit";
    /** 租户服务 | 策略模块 | 数据源管理 | 修改状态 */
    String TE_SOURCE_ES = "tenant:source:es";
    /** 租户服务 | 策略模块 | 数据源管理 | 删除 */
    String TE_SOURCE_DEL = "tenant:source:delete";
    /** 租户服务 | 策略模块 | 数据源管理 | 导入 */
    String TE_SOURCE_IMPORT = "tenant:source:import";
    /** 租户服务 | 策略模块 | 数据源管理 | 导出 */
    String TE_SOURCE_EXPORT = "tenant:source:export";

    /** 租户服务 | 策略模块 | 数据源策略管理 | 列表 */
    String TE_STRATEGY_LIST = "tenant:strategy:list";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 详情 */
    String TE_STRATEGY_SINGLE = "tenant:strategy:single";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 新增 */
    String TE_STRATEGY_ADD = "tenant:strategy:add";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 修改 */
    String TE_STRATEGY_EDIT = "tenant:strategy:edit";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 修改状态 */
    String TE_STRATEGY_ES = "tenant:strategy:es";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 删除 */
    String TE_STRATEGY_DEL = "tenant:strategy:delete";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 导入 */
    String TE_STRATEGY_IMPORT = "tenant:strategy:import";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 导出 */
    String TE_STRATEGY_EXPORT = "tenant:strategy:export";

}
