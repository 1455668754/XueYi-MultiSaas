package com.xueyi.common.core.utils.core;

import java.time.LocalDate;

/**
 * LocalDateTime工具类
 *
 * @author xueyi
 */
public class LocalDateTimeUtil extends cn.hutool.core.date.LocalDateTimeUtil {

    public static LocalDate parseDate(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        LocalDate date = null;
        try {
            date = cn.hutool.core.date.LocalDateTimeUtil.parseDate(text);
        } catch (Exception e) {
            try {
                String replacement = "$1";
                String regex = ".*(\\d{4}-\\d{2}-\\d{2}).*";
                if (StrUtil.matches(text, regex)) {
                    text = StrUtil.replace(text, regex, replacement);
                    System.out.println("1");
                    date = parseDate(text, "yyyy-MM-dd");
                }
                regex = ".*(\\d{4}/\\d{2}/\\d{2}).*";
                if (StrUtil.matches(text, regex)) {
                    text = StrUtil.replace(text, regex, replacement);
                    System.out.println("3");
                    date = parseDate(text, "yyyy/MM/dd");
                }
                regex = ".*(\\d{4}年\\d{2}月\\d{2}日).*";
                if (StrUtil.matches(text, regex)) {
                    text = StrUtil.replace(text, regex, replacement);
                    System.out.println("5");
                    date = parseDate(text, "yyyy年MM月dd日");
                }
                regex = ".*(\\d{4}年\\d月\\d{2}日).*";
                if (StrUtil.matches(text, regex)) {
                    text = StrUtil.replace(text, regex, replacement);
                    System.out.println("6");
                    date = parseDate(text, "yyyy年M月dd日");
                }
                regex = ".*(\\d{4}年\\d{2}月\\d日).*";
                if (StrUtil.matches(text, regex)) {
                    text = StrUtil.replace(text, regex, replacement);
                    System.out.println("7");
                    date = parseDate(text, "yyyy年MM月d日");
                }
                regex = ".*(\\d{4}年\\d月\\d日).*";
                if (StrUtil.matches(text, regex)) {
                    text = StrUtil.replace(text, regex, replacement);
                    System.out.println("8");
                    date = parseDate(text, "yyyy年M月d日");
                }
            } catch (Exception ignored) {
            }
        }
        return date;
    }
}
