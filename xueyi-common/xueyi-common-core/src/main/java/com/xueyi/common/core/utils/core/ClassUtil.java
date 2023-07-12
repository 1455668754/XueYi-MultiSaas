package com.xueyi.common.core.utils.core;

import java.util.Collection;

/**
 * 类工具类
 *
 * @author xueyi
 */
public class ClassUtil extends cn.hutool.core.util.ClassUtil {

    public static boolean notEqual(Class<?> clazz1, Class<?> clazz2) {
        return !equals(clazz1, clazz2);
    }

    public static boolean equals(Class<?> clazz1, Class<?> clazz2) {
        if (ObjectUtil.hasNull(clazz1, clazz2) && !ObjectUtil.isAllEmpty(clazz1, clazz2)) {
            return Boolean.FALSE;
        }
        ClassLoader loader1 = clazz1.getClassLoader();
        ClassLoader loader2 = clazz2.getClassLoader();
        return ObjectUtil.equals(loader1, loader2) && ObjectUtil.equals(clazz1.getName(), clazz2.getName()) && ObjectUtil.equals(clazz1.getPackageName(), clazz2.getPackageName());
    }

    /**
     * 判断是否非数组类型
     *
     * @param clazz 类型
     * @return 结果
     */
    public static boolean isNotArray(Class<?> clazz) {
        return !isArray(clazz);
    }

    /**
     * 判断是否是数组类型
     *
     * @param clazz 类型
     * @return 结果
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    /**
     * 判断是否是集合类型
     *
     * @param clazz 类型
     * @return 结果
     */
    public static boolean isNotCollection(Class<?> clazz) {
        return !isCollection(clazz);
    }

    /**
     * 判断是否是集合类型
     *
     * @param clazz 类
     * @return 是否为基本类型
     */
    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否非基本类型（包括包装类和原始类）或String
     *
     * @param clazz 类型
     * @return 是否非基本类型或String
     */
    public static boolean isSimpleType(Class<?> clazz) {
        return isBasicType(clazz) || isSimpleValueType(clazz);
    }

    /**
     * 判断是否非基本类型（包括包装类和原始类）或String
     *
     * @param clazz 类型
     * @return 是否非基本类型或String
     */
    public static boolean isNotSimpleType(Class<?> clazz) {
        return !isSimpleType(clazz);
    }

    /**
     * 是否非标准的类
     * 这个类必须：
     * 1、非接口
     * 2、非抽象类
     * 3、非Enum枚举
     * 4、非数组
     * 5、非注解
     * 6、非原始类型（int, long等）
     *
     * @param clazz 类
     * @return 是否非标准类
     */
    public static boolean isNotNormalClass(Class<?> clazz) {
        return !isNormalClass(clazz);
    }

    /**
     * 检查目标类是否不可以从原类转化
     * 转化包括：
     * 1、原类是对象，目标类型是原类型实现的接口
     * 2、目标类型是原类型的父类
     * 3、两者是原始类型或者包装类型（相互转换）
     *
     * @param targetType 目标类型
     * @param sourceType 原类型
     * @return 是否不可转化
     */
    public static boolean isNotAssignable(Class<?> targetType, Class<?> sourceType) {
        return !isAssignable(targetType, sourceType);
    }
}