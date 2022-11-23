package com.xueyi.common.core.constant.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工具类异常通用常量
 *
 * @author xueyi
 */
public class UtilErrorConstants {

    /** 校验内容 */
    @Getter
    @AllArgsConstructor
    public enum MergeUtil {

        FIELD_NULL("400", "分组{}的主子映射关系未正常映射，请检查{}是否正常进行注解！"),
        RECEIVE_KEY_TYPE_ERROR("400", "值接收键的类型必须是对象或者集合！");

        private final String code;
        private final String info;

    }
}
