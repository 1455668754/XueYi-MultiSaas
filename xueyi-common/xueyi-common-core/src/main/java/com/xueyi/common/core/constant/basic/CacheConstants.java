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

    public final static String AUTHORIZATION = "token";

    /** oauth 客户端信息 */
    public final static String CLIENT_DETAILS_KEY = "client:details";

    /** 缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum CacheType {
        CLIENT_DETAILS_KEY("client:details", "oauth 客户端信息"),
        CAPTCHA_CODE_KEY("captcha_codes:", "验证码"),
        SYS_DICT_KEY("system:dict", "字典"),
        SYS_CONFIG_KEY("system:config", "参数"),
        TE_STRATEGY_KEY("system:strategy", "源策略组"),
        TE_SOURCE_KEY("system:source", "数据源");

        private final String code;
        private final String info;

    }

    /** 登录缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum LoginTokenType {

        ADMIN("login_tokens", "管理端"),
        MEMBER("login_member_tokens", "会员端");

        private final String code;
        private final String info;

    }

}
