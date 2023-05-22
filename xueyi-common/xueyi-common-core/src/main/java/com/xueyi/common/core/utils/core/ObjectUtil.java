package com.xueyi.common.core.utils.core;

/**
 * 对象工具类
 *
 * @author xueyi
 */
public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    public static boolean isAllNotNull(Object... objs) {
        return !hasNull(objs);
    }
}