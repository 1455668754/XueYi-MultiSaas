package com.xueyi.common.core.utils.core;

import java.util.Collection;

/**
 * 集合工具类
 *
 * @author xueyi
 */
public class CollUtil extends cn.hutool.core.collection.CollUtil {

    public static boolean isNotNull(Collection<?> collection) {
        return !isNull(collection);
    }

    public static boolean isNull(Collection<?> collection) {
        return collection == null;
    }

    public static boolean notContains(Collection<?> collection, Object value) {
        return !contains(collection, value);
    }

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}