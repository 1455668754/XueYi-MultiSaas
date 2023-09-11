package com.xueyi.common.web.correlate.contant;

import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型通用常量
 *
 * @author xueyi
 */
public class CorrelateConstants {

    /** 服务层 - 操作类型 */
    @Getter
    @AllArgsConstructor
    public enum ServiceType {

        SELECT("SELECT", "默认查询"),
        SELECT_LIST("SELECT_LIST", "列表查询"),
        SELECT_ID_LIST("SELECT_ID_LIST", "Id单条查询"),
        SELECT_ID_SINGLE("SELECT_ID_SINGLE", "Id批量查询"),
        ADD("ADD", "新增"),
        BATCH_ADD("BATCH_ADD", "批量新增"),
        EDIT("EDIT", "修改"),
        BATCH_EDIT("BATCH_EDIT", "批量修改"),
        EDIT_STATUS("EDIT_STATUS", "修改状态"),
        DELETE("DELETE", "删除"),
        BATCH_DELETE("BATCH_DELETE", "批量删除"),
        CACHE_REFRESH("CACHE_REFRESH", "缓存更新");

        private final String code;
        private final String info;

        /** 是否为单条操作 */
        public boolean isSingle() {
            return StrUtil.equalsAny(name(), ADD.name(), EDIT.name(), EDIT_STATUS.name(), DELETE.name());
        }

        /** 是否为批量操作 */
        public boolean isBatch() {
            return StrUtil.equalsAny(name(), BATCH_ADD.name(), BATCH_EDIT.name(), BATCH_DELETE.name());
        }
    }

    /** 数据类型 */
    @Getter
    @AllArgsConstructor
    public enum DataRow {

        SINGLE("single", "单条数据"),
        LIST("list", "数据集合");

        private final String code;
        private final String info;

    }

    /** 主子关联 - 字段关联类型 */
    @Getter
    @AllArgsConstructor
    public enum SubFieldType {

        ONE_ON_ONE("one_on_one", "一对一"),
        ONE_TO_MANY("one_to_many", "一对多");

        private final String code;
        private final String info;
    }

    /** 主子关联 - 表关联类型 */
    @Getter
    @AllArgsConstructor
    public enum SubTableType {

        DIRECT("direct", "直接连接"),
        INDIRECT("indirect", "间接连接"),
        REMOTE("remote", "远程连接");

        private final String code;
        private final String info;

    }

    /** 主子关联 - 间接关联类型 */
    @Getter
    @AllArgsConstructor
    public enum MergeType {

        DIRECT("direct", "直接连接 | 主键仅一个值"),
        INDIRECT("indirect", "间接连接 | 主键为集合");

        private final String code;
        private final String info;

        public boolean isDirect() {
            return StrUtil.equals(name(), DIRECT.name());
        }

        public boolean isIndirect() {
            return StrUtil.equals(name(), INDIRECT.name());
        }
    }

    /** 主子关联 - 删除校验 */
    @Getter
    @AllArgsConstructor
    public enum SubDeleteType {

        NORMAL("normal", "存在子数据则无法删除！"),
        DIRECTLY("directly", "直接删除");

        private final String code;
        private final String info;
    }

    /** 主子关联 - 操作类型 */
    @Getter
    @AllArgsConstructor
    public enum SubOperate {

        SELECT("select", "查询类型"),
        ADD("insert", "新增类型"),
        EDIT("update", "修改类型"),
        DELETE("delete", "删除类型");

        private final String code;
        private final String info;
    }
}
