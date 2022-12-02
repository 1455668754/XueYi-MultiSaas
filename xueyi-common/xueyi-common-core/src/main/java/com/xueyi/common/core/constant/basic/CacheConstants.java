package com.xueyi.common.core.constant.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存的key通用常量
 *
 * @author xueyi
 */
public class CacheConstants {

    /** 缓存有效期，默认720（分钟） */
    public final static long EXPIRATION = 720;

    /** 缓存刷新时间，默认120（分钟） */
    public final static long REFRESH_TIME = 120;

    /** 权限缓存前缀 */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /** 验证码 redis key */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /** 登录缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum LoginTokenType {

        ADMIN("login_tokens:", "管理端");

        private final String code;
        private final String info;

    }

}
