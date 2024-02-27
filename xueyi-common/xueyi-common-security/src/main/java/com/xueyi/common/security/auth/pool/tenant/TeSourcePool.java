package com.xueyi.common.security.auth.pool.tenant;

/**
 * 租户服务 | 策略模块 权限标识常量
 *
 * @author xueyi
 */
public interface TeSourcePool {

    /** 租户服务 | 策略模块 | 数据源管理 | 列表 */
    String TE_SOURCE_LIST = "RD:tenant:source:source:list";
    /** 租户服务 | 策略模块 | 数据源管理 | 详情 */
    String TE_SOURCE_SINGLE = "RD:tenant:source:source:single";
    /** 租户服务 | 策略模块 | 数据源管理 | 新增 */
    String TE_SOURCE_ADD = "RD:tenant:source:source:add";
    /** 租户服务 | 策略模块 | 数据源管理 | 修改 */
    String TE_SOURCE_EDIT = "RD:tenant:source:source:edit";
    /** 租户服务 | 策略模块 | 数据源管理 | 状态修改 */
    String TE_SOURCE_ES = "RD:tenant:source:source:es";
    /** 租户服务 | 策略模块 | 数据源管理 | 删除 */
    String TE_SOURCE_DEL = "RD:tenant:source:source:del";

    /** 租户服务 | 策略模块 | 数据源策略管理 | 列表 */
    String TE_STRATEGY_LIST = "RD:tenant:source:strategy:list";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 详情 */
    String TE_STRATEGY_SINGLE = "RD:tenant:source:strategy:single";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 新增 */
    String TE_STRATEGY_ADD = "RD:tenant:source:strategy:add";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 修改 */
    String TE_STRATEGY_EDIT = "RD:tenant:source:strategy:edit";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 状态修改 */
    String TE_STRATEGY_ES = "RD:tenant:source:strategy:es";
    /** 租户服务 | 策略模块 | 数据源策略管理 | 删除 */
    String TE_STRATEGY_DEL = "RD:tenant:source:strategy:del";

}
