package com.xueyi.common.log.enums;

/**
 * 业务操作类型
 *
 * @author xueyi
 */
public enum BusinessType {
    
    /** 其它 */
    OTHER,

    /** 新增 */
    INSERT,

    /** 强制新增 */
    INSERT_FORCE,

    /** 修改 */
    UPDATE,

    /** 权限修改 */
    AUTH,

    /** 强制修改 */
    UPDATE_FORCE,

    /** 修改状态 */
    UPDATE_STATUS,

    /** 强制修改状态 */
    UPDATE_STATUS_FORCE,

    /** 删除 */
    DELETE,

    /** 强制删除 */
    DELETE_FORCE,

    /** 授权 */
    GRANT,

    /** 导出 */
    EXPORT,

    /** 导入 */
    IMPORT,

    /** 强退 */
    FORCE,

    /** 生成代码 */
    GENCODE,

    /** 清空数据 */
    CLEAN,
}
