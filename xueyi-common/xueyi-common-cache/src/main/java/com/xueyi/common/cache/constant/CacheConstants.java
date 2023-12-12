package com.xueyi.common.cache.constant;

import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.EnumUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import com.xueyi.system.api.dict.feign.RemoteDictService;
import com.xueyi.tenant.api.source.feign.RemoteSourceService;
import com.xueyi.tenant.api.source.feign.RemoteStrategyService;
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

    /** 缓存有效期，默认360（分钟） */
    public final static long ACCESS_TIME = 360;

    /** 缓存刷新时间，默认120（分钟） */
    public final static long REFRESH_TIME = 120;

    public final static String AUTHORIZATION = "token";

    /** oauth 客户端信息 */
    public final static String CLIENT_DETAILS_KEY = "client:details";

    /** 参数类型 */
    @Getter
    @AllArgsConstructor
    public enum ConfigType {

        USER_INIT_PASSWORD("sys.user.initPassword", "用户管理-账号初始密码", String.class, StrUtil.EMPTY),
        SYS_CODE_SHOW("sys.code.show", "系统模块:数据编码配置:功能开关（Y启用 N禁用）", String.class, DictConstants.DicYesNo.NO.getCode()),
        SYS_CODE_MUST("sys.code.must", "系统模块:数据编码配置:必须字段（Y是 N否）", String.class, DictConstants.DicYesNo.NO.getCode());

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

        ROUTE_DICT_KEY("system:dict:route", false, "字典路由", () -> SpringUtil.getBean(RemoteDictService.class).refreshCommonCacheInner()),
        SYS_DICT_KEY("system:dict:tenant", true, "租户字典", () -> SpringUtil.getBean(RemoteDictService.class).refreshCacheInner()),
        TE_DICT_KEY("system:dict:tenant", true, "通用字典", () -> SpringUtil.getBean(RemoteDictService.class).refreshCommonCacheInner()),
        ROUTE_CONFIG_KEY("system:config:route", false, "参数路由", () -> SpringUtil.getBean(RemoteConfigService.class).refreshTeCacheInner()),
        SYS_CONFIG_KEY("system:config:tenant", true, "租户参数", () -> SpringUtil.getBean(RemoteConfigService.class).refreshCacheInner()),
        TE_CONFIG_KEY("system:config:tenant", true, "通用参数", () -> SpringUtil.getBean(RemoteDictService.class).refreshCommonCacheInner()),
        TE_STRATEGY_KEY("system:strategy", false, "源策略组", () -> SpringUtil.getBean(RemoteStrategyService.class).refreshCacheInner()),
        TE_SOURCE_KEY("system:source", false, "数据源", () -> SpringUtil.getBean(RemoteSourceService.class).refreshCacheInner()),
        TE_TENANT_KEY("system:tenant", false, "租户", () -> SpringUtil.getBean(RemoteTenantService.class).refreshCacheInner());

        private final String code;
        private final Boolean isTenant;
        private final String info;
        private final Supplier<Object> consumer;

        /**
         * 获取缓存键值
         *
         * @return 缓存键值
         */
        public String getCacheKey() {
            return getCusCacheKey(getCode(), getIsTenant());
        }

        /**
         * 获取缓存键值 | 指定企业Id
         *
         * @param enterpriseId 企业Id
         * @return 缓存键值
         */
        public String getCacheKey(Long enterpriseId) {
            return getCusCacheKey(getCode(), getIsTenant(), enterpriseId);
        }

        /**
         * 获取缓存键值 | 自定义
         *
         * @param code 缓存编码
         * @return 缓存键值
         */
        public static String getCusCacheKey(String code) {
            return getCusCacheKey(code, Boolean.FALSE, null);
        }

        /**
         * 获取缓存键值 | 自定义
         *
         * @param code     缓存编码
         * @param isTenant 租户级缓存
         * @return 缓存键值
         */
        public static String getCusCacheKey(String code, Boolean isTenant) {
            return getCusCacheKey(code, isTenant, isTenant ? SecurityContextHolder.getEnterpriseId() : null);
        }

        /**
         * 获取缓存键值 | 自定义
         *
         * @param code         缓存编码
         * @param isTenant     租户级缓存
         * @param enterpriseId 企业Id
         * @return 缓存键值
         */
        public static String getCusCacheKey(String code, Boolean isTenant, Long enterpriseId) {
            String cacheKey;
            if (isTenant) {
                if (ObjectUtil.isNull(enterpriseId)) {
                    throw new ServiceException(StrUtil.format("缓存键{}为企业级缓存，企业Id不能为空", code));
                }
                cacheKey = StrUtil.format("{}:{}", code, enterpriseId);
            } else {
                cacheKey = code;
            }
            return cacheKey;
        }
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
