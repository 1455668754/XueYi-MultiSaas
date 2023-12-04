package com.xueyi.common.core.utils.core;

import java.time.LocalDate;

/**
 * LocalDateTime工具类
 *
 * @author xueyi
 */
public class LocalDateTimeUtil extends cn.hutool.core.date.LocalDateTimeUtil {

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
                if (ObjectUtil.isNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}/\\d{2}/\\d{2}).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy/MM/dd");
                }
                if (ObjectUtil.isNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d{2}月\\d{2}日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年MM月dd日");
                }
                if (ObjectUtil.isNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d月\\d{2}日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年M月dd日");
                }
                if (ObjectUtil.isNull(date)) {
                    return date;
                }
                regex = ".*(\\d{4}年\\d{2}月\\d日).*";
                if (time.matches(regex)) {
                    time = time.replaceAll(regex, replacement);
                    date = LocalDateTimeUtil.parseDate(time, "yyyy年MM月d日");
                }
                if (ObjectUtil.isNull(date)) {
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
}
