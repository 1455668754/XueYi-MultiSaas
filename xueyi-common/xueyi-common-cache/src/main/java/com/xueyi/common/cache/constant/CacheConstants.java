package com.xueyi.common.cache.constant;

import com.xueyi.common.core.utils.core.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 缓存通用常量
 *
 * @author xueyi
 */
public class CacheConstants {

    /** 缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum CacheType {

        SYS_DICT_KEY("system:dict", "字典"),
        SYS_CONFIG_KEY("system:config", "参数"),
        TE_STRATEGY_KEY("system:strategy", "源策略组"),
        TE_SOURCE_KEY("system:source", "数据源");

        private final String code;
        private final String info;

        public static CacheType getByCode(String code) {
            return Objects.requireNonNull(EnumUtil.getByCode(CacheType.class, code));
        }

    }

}
