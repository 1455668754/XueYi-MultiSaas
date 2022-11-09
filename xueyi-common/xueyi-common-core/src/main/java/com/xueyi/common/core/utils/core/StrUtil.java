package com.xueyi.common.core.utils.core;

import com.xueyi.common.core.utils.core.pool.StrPool;

/**
 * 字符串工具类
 *
 * @author xueyi
 */
public class StrUtil extends cn.hutool.core.util.StrUtil implements StrPool {

    /**
     * 截取分隔字符串之前的字符串 | 不包括分隔字符串
     *
     * @param str       字符串
     * @param separator 分隔字符串
     * @return 字符串
     */
    public static String subBefore(CharSequence str, CharSequence separator) {
        return subBefore(str, separator, false);
    }

    /**
     * 截取最后一个分隔字符串之前的字符串 | 不包括分隔字符串
     *
     * @param str       字符串
     * @param separator 分隔字符串
     * @return 字符串
     */
    public static String subBeforeLast(CharSequence str, CharSequence separator) {
        return subBefore(str, separator, true);
    }

    /**
     * 截取分隔字符串之后的字符串 | 不包括分隔字符串
     *
     * @param str       字符串
     * @param separator 分隔字符串
     * @return 字符串
     */
    public static String subAfter(CharSequence str, CharSequence separator) {
        return subAfter(str, separator, false);
    }

    /**
     * 截取最后一个分隔字符串之后的字符串 | 不包括分隔字符串
     *
     * @param str       字符串
     * @param separator 分隔字符串
     * @return 字符串
     */
    public static String subAfterLast(CharSequence str, CharSequence separator) {
        return subAfter(str, separator, true);
    }

    /**
     * 是否包含字符串
     *
     * @param str    验证字符串
     * @param strArr 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strArr) {
        if (str != null && strArr != null)
            for (String s : strArr)
                if (str.equalsIgnoreCase(trim(s)))
                    return true;
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return EMPTY;
        } else if (!name.contains(UNDERLINE)) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split(UNDERLINE);
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 字符串全部大写 例如：HelLo->HELLO
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * 首字母大写 例如：hello->Hello
     *
     * @param str 字符串
     * @return 字符串
     */

    public static String capitalize(String str) {
        return upperFirst(str);
    }

    /**
     * 首字母小写 例如：Hello->hello
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String uncapitalize(String str) {
        return lowerFirst(str);
    }
}
