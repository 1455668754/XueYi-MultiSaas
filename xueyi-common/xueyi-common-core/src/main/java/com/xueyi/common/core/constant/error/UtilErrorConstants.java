package com.xueyi.common.core.constant.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工具类异常通用常量
 *
 * @author xueyi
 */
public class UtilErrorConstants {

    /** 主从关联工具类 - 校验内容 */
    @Getter
    @AllArgsConstructor
    public enum MergeError {
        MERGE_PO_CLASS_EQUAL("400", "关联类对象class与Mapper对应Class不一致！"),
        CORRELATION_ERROR("400", "分组{}中，映射对象配置错误，请检查代码！"),
        RECEIVE_DIRECT_TYPE_ERROR("400", "分组{}中，直接关联时，主数据对象中，关联数据接收字段的类型必须是对象或者集合！"),
        RECEIVE_INDIRECT_TYPE_ERROR("400", "分组{}中，间接关联时，主数据对象中，关联数据接收字段的类型必须是集合！"),
        RECEIVE_ARR_TYPE_ERROR("400", "分组{}中，主数据对象中，关联数据键值接收字段的类型必须是数组或者集合！");

        private final String code;
        private final String info;

    }
}
