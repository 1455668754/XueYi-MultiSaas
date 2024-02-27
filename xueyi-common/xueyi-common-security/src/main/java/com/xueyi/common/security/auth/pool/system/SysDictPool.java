package com.xueyi.common.security.auth.pool.system;

/**
 * 系统服务 | 字典模块 权限标识常量
 *
 * @author xueyi
 */
public interface SysDictPool {

    /** 系统服务 | 字典模块 | 字典管理 | 列表 */
    String SYS_DICT_LIST = "RD:system:dict:dict:list";
    /** 系统服务 | 字典模块 | 字典管理 | 详情 */
    String SYS_DICT_SINGLE = "RD:system:dict:dict:single";
    /** 系统服务 | 字典模块 | 字典管理 | 新增 */
    String SYS_DICT_ADD = "RD:system:dict:dict:add";
    /** 系统服务 | 字典模块 | 字典管理 | 修改 */
    String SYS_DICT_EDIT = "RD:system:dict:dict:edit";
    /** 系统服务 | 字典模块 | 字典管理 | 状态修改 */
    String SYS_DICT_ES = "RD:system:dict:dict:es";
    /** 系统服务 | 字典模块 | 字典管理 | 删除 */
    String SYS_DICT_DEL = "RD:system:dict:dict:del";
    /** 系统服务 | 字典模块 | 字典管理 | 字典管理 */
    String SYS_DICT_DICT = "RD:system:dict:dict:dict";

    /** 系统服务 | 字典模块 | 参数管理 | 列表 */
    String SYS_CONFIG_LIST = "RD:system:dict:config:list";
    /** 系统服务 | 字典模块 | 参数管理 | 详情 */
    String SYS_CONFIG_SINGLE = "RD:system:dict:config:single";
    /** 系统服务 | 字典模块 | 参数管理 | 新增 */
    String SYS_CONFIG_ADD = "RD:system:dict:config:add";
    /** 系统服务 | 字典模块 | 参数管理 | 修改 */
    String SYS_CONFIG_EDIT = "RD:system:dict:config:edit";
    /** 系统服务 | 字典模块 | 参数管理 | 状态修改 */
    String SYS_CONFIG_ES = "RD:system:dict:config:es";
    /** 系统服务 | 字典模块 | 参数管理 | 删除 */
    String SYS_CONFIG_DEL = "RD:system:dict:config:del";

}
