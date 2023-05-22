package com.xueyi.common.redis.constant;

import com.xueyi.common.core.utils.core.EnumUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存 通用常量
 *
 * @author xueyi
 */
public class RedisConstants {

    /** 缓存键 */
    @Getter
    @AllArgsConstructor
    public enum CacheKey {

        CLIENT_DETAILS_KEY("client:details", "oauth 客户端信息"),
        CAPTCHA_CODE_KEY("captcha_codes:", "验证码"),
        SYS_CORRELATE_KEY("system:correlate:{}.{}", "数据关联工具"),
        SERVICE_WX_MP_KEY("service:wxMp", "服务：微信公众号"),
        SERVICE_WX_MA_KEY("service:wxMa", "服务：微信小程序"),
        SERVICE_WX_PAY_KEY("service:wxPay", "服务：微信支付");

        private final String code;
        private final String info;

        public String getCacheName(Object... cacheNames) {
            return StrUtil.format(getCode(), cacheNames);
        }
    }

    /** 缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum OperateType {

        REFRESH_ALL("refresh_all", "更新全部"),
        REFRESH("refresh", "更新"),
        REMOVE("remove", "删除");

        private final String code;
        private final String info;

        public static OperateType getByCode(String code) {
            return EnumUtil.getByCode(OperateType.class, code);
        }

    }

}
