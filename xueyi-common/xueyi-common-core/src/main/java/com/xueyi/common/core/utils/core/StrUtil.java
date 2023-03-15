package com.xueyi.common.core.utils.core;

import cn.hutool.core.text.AntPathMatcher;
import com.xueyi.common.core.utils.core.pool.StrPool;
import org.springframework.lang.Nullable;

/**
 * 字符串工具类
 *
 * @author xueyi
 */
public class StrUtil extends cn.hutool.core.util.StrUtil implements StrPool {

    public static boolean notEquals(CharSequence str1, CharSequence str2) {
        return !equals(str1, str2, false);
    }

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

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str     指定字符串
     * @param strList 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, String... strList) {
        if (isEmpty(str) || ArrayUtil.isEmpty(strList))
            return false;
        for (String pattern : strList)
            if (isMatch(pattern, str))
                return true;
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return 结果
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    public static boolean hasText(@Nullable CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    public static boolean hasText(@Nullable String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; ++i)
            if (!Character.isWhitespace(str.charAt(i)))
                return true;
        return false;
    }
}