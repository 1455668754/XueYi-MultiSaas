package com.xueyi.common.core.utils.core;

import java.util.Collection;

/**
 * 类工具类
 *
 * @author xueyi
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {

    /**
     * 判断返回值类型是否是集合类型
     *
     * @param fieldType 类型
     * @return 结果
     */
    public static boolean isCollection(Class<?> fieldType) {
        return Collection.class.isAssignableFrom(fieldType);
    }
}
