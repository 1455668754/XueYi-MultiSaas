package com.xueyi.common.core.constant.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 临时缓存的key通用常量
 *
 * @author xueyi
 */
public class RedisConstants {

    /** 缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum CacheKey {

        CAPTCHA_CODE_KEY("captcha_codes:", "验证码");

        private final String code;
        private final String info;

    }
}
