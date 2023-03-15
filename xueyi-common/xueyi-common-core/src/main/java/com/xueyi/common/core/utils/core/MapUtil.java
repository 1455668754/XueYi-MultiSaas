package com.xueyi.common.core.utils.core;

import java.util.Map;

/**
 * Map工具类
 *
 * @author xueyi
 */
public class MapUtil extends cn.hutool.core.map.MapUtil {

    public static boolean isNotNull(Map<?, ?> map) {
        return !isNull(map);
    }

    public static boolean isNull(Map<?, ?> map) {
        return null == map;
    }

}