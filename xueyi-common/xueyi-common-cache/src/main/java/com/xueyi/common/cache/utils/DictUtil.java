package com.xueyi.common.cache.utils;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.cache.service.CacheService;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import com.xueyi.system.api.dict.feign.RemoteDictService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典缓存管理工具类
 *
 * @author xueyi
 */
public class DictUtil {

    private static CacheService cacheService;

    private static CacheService getCacheService() {
        if (ObjectUtil.isNull(cacheService)) {
            cacheService = SpringUtil.getBean(CacheService.class);
        }
        return cacheService;
    }

    /**
     * 获取参数缓存
     *
     * @param type 参数类型
     * @return 参数数据
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T getConfigCache(CacheConstants.ConfigType type) {
        SysConfigDto config = getCacheService().getCacheObject(CacheConstants.CacheType.ROUTE_CONFIG_KEY, type.getCode());
        if (ObjectUtil.isNull(config)) {
            return null;
        }
        String value;
        if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), config.getCacheType())) {
            value = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_CONFIG_KEY, type.getCode());
            if (ObjectUtil.isNull(value)) {
                SpringUtil.getBean(RemoteConfigService.class).syncCacheInner();
                value = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_CONFIG_KEY, type.getCode());
            }
        } else {
            SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
            value = getCacheService().getCacheObject(CacheConstants.CacheType.TE_CONFIG_KEY, type.getCode());
            SecurityContextHolder.rollLastEnterpriseId();
        }
        return (T) ConvertUtil.convert(type.getClazz(), value, type.getDefaultValue());
    }

    /**
     * 获取字典缓存
     *
     * @param codes 字典编码集合
     * @return 字典数据列表
     */
    public static Map<String, List<SysDictDataDto>> getDictCache(Collection<String> codes) {
        Map<String, List<SysDictDataDto>> map = new HashMap<>();
        codes.forEach(code -> map.put(code, getDictCache(code)));
        return map;
    }

    /**
     * 获取字典缓存
     *
     * @param code 字典编码
     * @return 字典数据列表
     */
    public static List<SysDictDataDto> getDictCache(String code) {
        SysDictTypeDto type = getCacheService().getCacheObject(CacheConstants.CacheType.ROUTE_DICT_KEY, code);
        if (ObjectUtil.isNull(type)) {
            return null;
        }
        List<SysDictDataDto> dictList;
        if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), type.getCacheType())) {
            dictList = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_DICT_KEY, code);
            if (ObjectUtil.isNull(dictList)) {
                SpringUtil.getBean(RemoteDictService.class).syncCacheInner();
                dictList = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_DICT_KEY, code);
            }
        } else {
            SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
            dictList = getCacheService().getCacheObject(CacheConstants.CacheType.TE_DICT_KEY, code);
            SecurityContextHolder.rollLastEnterpriseId();
        }
        return dictList;
    }
}
