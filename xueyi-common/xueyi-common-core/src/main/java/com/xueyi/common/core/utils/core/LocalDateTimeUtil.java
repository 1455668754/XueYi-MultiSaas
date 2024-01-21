package com.xueyi.common.core.utils.core;

import cn.hutool.core.date.DatePattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LocalDateTime工具类
 *
 * @author xueyi
 */
public class LocalDateTimeUtil extends cn.hutool.core.date.LocalDateTimeUtil {

    /**
     * 时间字符串转LocalDate
     *
     * @param time 时间字符串
     * @return LocalDate
     */
    public static LocalDate parseDate(String time) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        LocalDate date = null;
        try {
            date = cn.hutool.core.date.LocalDateTimeUtil.parseDate(time);
        } catch (Exception e) {
            try {
                String replacement = "$1";
                String regex = ".*(\\d{4}-\\d{2}-\\d{2}).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy-MM-dd");
                }
                if (ObjectUtil.isNotNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}/\\d{2}/\\d{2}).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy/MM/dd");
                }
                if (ObjectUtil.isNotNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d{2}月\\d{2}日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年MM月dd日");
                }
                if (ObjectUtil.isNotNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d月\\d{2}日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年M月dd日");
                }
                if (ObjectUtil.isNotNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d{2}月\\d日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年MM月d日");
                }
                if (ObjectUtil.isNotNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d月\\d日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年M月d日");
                }
            } catch (Exception ignored) {
            }
        }
        return date;
    }

    /**
     * 时间字符串转LocalDateTime
     *
     * @param text 时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String text) {
        if (StrUtil.isNotEmpty(text) && text.length() <= 10)
            return parse(text, DatePattern.NORM_DATE_PATTERN);
        else
            return parse(text, DatePattern.NORM_DATETIME_PATTERN);
    }
}
