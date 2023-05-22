package com.xueyi.common.cache.constants;

import com.xueyi.common.core.utils.core.EnumUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import com.xueyi.system.api.dict.feign.RemoteDictService;
import com.xueyi.tenant.api.source.feign.RemoteSourceService;
import com.xueyi.tenant.api.tenant.feign.RemoteStrategyService;
import com.xueyi.tenant.api.tenant.feign.RemoteTenantService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

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

    /** 参数类型 */
    @Getter
    @AllArgsConstructor
    public enum ConfigType {

        USER_INIT_PASSWORD("sys.user.initPassword", "用户管理-账号初始密码", String.class, StrUtil.EMPTY);

        private final String code;
        private final String info;
        private final Class<?> clazz;
        private final Object defaultValue;

        public static ConfigType getByCode(String code) {
            return EnumUtil.getByCode(ConfigType.class, code);
        }
    }
    /** 缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum CacheType {

        ROUTE_DICT_KEY("system:dict:route", "字典路由", () -> SpringUtil.getBean(RemoteDictService .class).refreshTeCacheInner()),
        SYS_DICT_KEY("system:dict:tenant:{}", "租户字典", () -> SpringUtil.getBean(RemoteDictService.class).refreshCacheInner()),
        TE_DICT_KEY("system:dict:tenant:{}", "通用字典", () ->SpringUtil.getBean(RemoteDictService.class).refreshTeCacheInner()),
        ROUTE_CONFIG_KEY("system:config:route", "参数路由", () -> SpringUtil.getBean(RemoteConfigService .class).refreshTeCacheInner()),
        SYS_CONFIG_KEY("system:config:tenant:{}", "租户参数", () -> SpringUtil.getBean(RemoteConfigService.class).refreshCacheInner()),
        TE_CONFIG_KEY("system:config:tenant:{}", "通用参数", () -> SpringUtil.getBean(RemoteDictService.class).refreshTeCacheInner()),
        TE_STRATEGY_KEY("system:strategy", "源策略组", () -> SpringUtil.getBean(RemoteStrategyService .class).refreshCacheInner()),
        TE_SOURCE_KEY("system:source", "数据源", () -> SpringUtil.getBean(RemoteSourceService .class).refreshCacheInner()),
        TE_TENANT_KEY("system:tenant", "租户", () -> SpringUtil.getBean(RemoteTenantService .class).refreshCacheInner());

        private final String code;
        private final String info;
        private final Supplier<Object> consumer;

    }

    /** 登录缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum LoginTokenType {

        ADMIN("login_tokens", "管理端"),
        MEMBER("login_member_tokens", "会员端"),
        PLATFORM("login_platform_tokens", "平台端");

        private final String code;
        private final String info;

    }

}
