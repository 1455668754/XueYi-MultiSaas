package com.xueyi.common.core.utils.core;

import com.xueyi.common.core.exception.UtilException;

import java.util.List;

/**
 * 枚举工具类
 *
 * @author xueyi
 */
public class EnumUtil extends cn.hutool.core.util.EnumUtil {

    /**
     * 根据编码获取枚举对象
     *
     * @param clazz 枚举class
     * @param code  内容
     * @return 枚举对象
     */
    public static <E extends Enum<E>> E getByCode(Class<E> clazz, String code) {
        return getByFieldName(clazz, "code", code, false);
    }

    /**
     * 根据编码获取枚举对象
     *
     * @param clazz 枚举class
     * @param code  内容
     * @return 枚举对象
     */
    public static <E extends Enum<E>> E getByCodeElseNull(Class<E> clazz, String code) {
        return getByFieldName(clazz, "code", code, true);
    }

    /**
     * 根据内容获取枚举对象
     *
     * @param clazz 枚举class
     * @param info  内容
     * @return 枚举对象
     */
    public static <E extends Enum<E>> E getByInfo(Class<E> clazz, String info) {
        return getByFieldName(clazz, "info", info, false);
    }

    /**
     * 根据指定字段获取枚举对象
     *
     * @param clazz       枚举class
     * @param fieldName   字段名
     * @param code        字段内容
     * @param defaultNull 是否默认为空
     * @return 枚举对象
     */
    public static <E extends Enum<E>> E getByFieldName(Class<E> clazz, String fieldName, String code, boolean defaultNull) {
        List<Object> codes = getFieldValues(clazz, fieldName);
        int[] indexArray = ListUtil.indexOfAll(codes, code::equals);
        if (ArrayUtil.isEmpty(indexArray))
            if (defaultNull) {
                return null;
            } else {
                throw new UtilException("未匹配到枚举值！");
            }
        else if (indexArray.length > NumberUtil.One)
            throw new UtilException("匹配到多个枚举值！");
        return getEnumAt(clazz, indexArray[NumberUtil.Zero]);
    }
}
