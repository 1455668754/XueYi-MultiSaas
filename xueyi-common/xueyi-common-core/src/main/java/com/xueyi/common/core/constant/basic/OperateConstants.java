package com.xueyi.common.core.constant.basic;

import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型通用常量
 *
 * @author xueyi
 */
public class OperateConstants {

    /** 服务层 - 操作类型 */
    @Getter
    @AllArgsConstructor
    public enum ServiceType {

        ADD("ADD", "新增"),
        BATCH_ADD("BATCH_ADD", "批量新增"),
        EDIT("EDIT", "修改"),
        BATCH_EDIT("BATCH_EDIT", "批量修改"),
        EDIT_STATUS("EDIT_STATUS", "修改状态"),
        DELETE("DELETE", "删除"),
        BATCH_DELETE("BATCH_DELETE", "批量删除");

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

    /** 主子关联 - 键关联类型 */
    @Getter
    @AllArgsConstructor
    public enum SubKeyType {

        MAIN_KEY("main_key", "主关联键"),
        SUB_KEY("sub_key", "子关联键"),
        RECEIVE_KEY("receive_key", "值接收键"),
        MERGE_MAIN_KEY("merge_main_key", "间接关联 - 关联主键"),
        MERGE_CLASS_KEY("merge_class_key", "间接关联 - 关联类class"),
        MERGE_PO_CLASS_KEY("merge_po_class_key", "间接关联 - 关联类对象class"),
        MERGE_SUB_KEY("merge_sub_key", "间接关联 - 关联子键"),
        RECEIVE_ARR_KEY("receive_arr_key", "间接关联 - 关联子键值接收键");

        private final String code;
        private final String info;

        public boolean isMainKey() {
            return StrUtil.equals(name(), MAIN_KEY.name());
        }

        public boolean isSubKey() {
            return StrUtil.equals(name(), SUB_KEY.name());
        }

        public boolean isReceiveKey() {
            return StrUtil.equals(name(), RECEIVE_KEY.name());
        }

        public boolean isMergeMainKey() {
            return StrUtil.equals(name(), MERGE_MAIN_KEY.name());
        }

        public boolean isMergeSubKey() {
            return StrUtil.equals(name(), MERGE_SUB_KEY.name());
        }

        public boolean isReceiveArrKey() {
            return StrUtil.equals(name(), RECEIVE_ARR_KEY.name());
        }
    }

    /** 数据类型 */
    @Getter
    @AllArgsConstructor
    public enum DataRow {

        SINGLE("single", "单条数据"),
        COLLECTION("collection", "数据集合");

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
        INDIRECT("indirect", "间接连接");

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
    public enum SubOperateType {

        EX_SEL("exclude select", "排除 查询"),
        EX_ADD("exclude insert", "排除 新增"),
        EX_EDIT("exclude update", "排除 修改"),
        EX_DEL("exclude delete", "排除 删除"),
        EX_SEL_OR_EDIT("exclude select or update", "排除 查询 and 修改"),
        EX_SEL_OR_ADD_OR_EDIT("exclude select or insert or update", "排除 查询 and 新增 and 修改");

        private final String code;
        private final String info;
    }
}
