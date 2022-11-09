package com.xueyi.common.core.utils.core;

/**
 * 对象工具类
 *
 * @author xueyi
 */
public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
